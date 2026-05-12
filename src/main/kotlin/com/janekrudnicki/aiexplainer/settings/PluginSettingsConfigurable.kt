package com.janekrudnicki.aiexplainer.settings

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBPasswordField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginSettingsConfigurable : Configurable {

    private var myPanel: JPanel? = null
    private val myApiKeyField = JBPasswordField()

    companion object {
        const val API_KEY_PROPERTY_NAME = "com.janekrudnicki.aiexplainer.GroqApiKey"

        fun getApiKey(): String? {
            return PropertiesComponent.getInstance().getValue(API_KEY_PROPERTY_NAME)
        }
    }

    override fun getDisplayName(): String = "AI Code Explainer"

    override fun createComponent(): JComponent? {
        myPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Groq API Key:", myApiKeyField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        return myPanel
    }

    override fun isModified(): Boolean {
        val savedKey = PropertiesComponent.getInstance().getValue(API_KEY_PROPERTY_NAME, "")
        return String(myApiKeyField.password) != savedKey
    }

    override fun apply() {
        PropertiesComponent.getInstance().setValue(API_KEY_PROPERTY_NAME, String(myApiKeyField.password))
    }

    override fun reset() {
        myApiKeyField.text = PropertiesComponent.getInstance().getValue(API_KEY_PROPERTY_NAME, "")
    }

    override fun disposeUIResources() {
        myPanel = null
    }
}