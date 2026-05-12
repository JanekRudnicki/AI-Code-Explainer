package com.janekrudnicki.aiexplainer.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.janekrudnicki.aiexplainer.service.AiExplainerService
import com.janekrudnicki.aiexplainer.settings.PluginSettingsConfigurable
import com.janekrudnicki.aiexplainer.ui.ExplanationPopup

class ExplainCodeAction : AnAction() {

    private val aiService = AiExplainerService()

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val project = e.project ?: return

        val apiKey = PluginSettingsConfigurable.getApiKey()
        if (apiKey.isNullOrEmpty()) {
            ExplanationPopup.show(editor, "<b>Please configure your Groq API key in Settings -> Tools -> AI Code Explainer</b>")
            return
        }

        val selectedText = editor.selectionModel.selectedText
        if (selectedText.isNullOrEmpty()) {
            ExplanationPopup.show(editor, "<b>Please select some code to explain.</b>")
            return
        }

        val prompt = buildPrompt(project, selectedText)

        object : Task.Backgroundable(project, "Explaining Code with AI", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.text = "Waiting for AI response..."
                try {
                    val explanation = aiService.getExplanation(apiKey, prompt)
                    ApplicationManager.getApplication().invokeLater {
                        ExplanationPopup.show(editor, explanation)
                    }
                } catch (ex: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        ExplanationPopup.show(editor, "<b>Error:</b> ${ex.message}")
                    }
                }
            }
        }.queue()
    }

    private fun buildPrompt(project: Project, selectedText: String): String {
        val contextFiles = getOpenFilesContext(project, limit = 3)
        return """
            Explain the following code fragment clearly and concisely:
            
            ```
            $selectedText
            ```
            
            Here is some context from other open files in the project to help you understand it better:
            
            $contextFiles
        """.trimIndent()
    }

    private fun getOpenFilesContext(project: Project, limit: Int): String {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val openFiles = fileEditorManager.openFiles.take(limit)

        val contextBuilder = StringBuilder()
        for (file in openFiles) {
            if (file.length < 1024 * 1024) {
                try {
                    val content = String(file.contentsToByteArray(), file.charset)
                    contextBuilder.append("--- File: ${file.name} ---\n")
                    contextBuilder.append(content)
                    contextBuilder.append("\n\n")
                } catch (ignored: Exception) {

                }
            }
        }
        return contextBuilder.toString()
    }
}