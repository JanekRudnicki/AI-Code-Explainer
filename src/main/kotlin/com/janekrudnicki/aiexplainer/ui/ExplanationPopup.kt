package com.janekrudnicki.aiexplainer.ui

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import java.awt.Point

object ExplanationPopup {

    fun show(editor: Editor, message: String) {
        val formattedMessage = if (message.startsWith("<b>")) {
            "<html>$message</html>"
        } else {
            "<html><body style='width: 400px;'>${message.replace("\n", "<br>")}</body></html>"
        }

        val popup = JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(formattedMessage, null, null, null)
            .setHideOnClickOutside(true)
            .setCloseButtonEnabled(true)
            .createBalloon()

        val position = JBPopupFactory.getInstance().guessBestPopupLocation(editor)

        val point = position.point ?: Point(0, 0)

        popup.show(position, Balloon.Position.below)
    }
}