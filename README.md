# AI Code Explainer - IntelliJ Plugin 🧠

A lightweight, well-structured IntelliJ IDEA plugin that explains selected code fragments using AI.
Developed as a technical task for the JetBrains Internship.

## 🎯 Task Overview
This project fulfills the requirement of creating a simple AI-related IntelliJ plugin.
While the functionality is straightforward, the primary focus during development was on **code quality, architectural structure, and clean Git history**.

## 🏗️ Architecture & Decisions
To avoid a chaotic "everything-in-one-file" approach, the plugin follows a clear separation of concerns:

1. **`action` (ExplainCodeAction)**
    - Handles the entry point from the IDE.
    - Gathers the selected text and up to 3 currently open files to build a rich context prompt for the AI.
    - Delegates network calls to a background thread (`Task.Backgroundable`) to prevent freezing the IntelliJ UI.

2. **`service` (AiExplainerService)**
    - A dedicated layer for external API communication (using the Groq API / Llama-3 model for fast inference).
    - Responsible for HTTP requests and JSON parsing using `Gson`.

3. **`ui` (ExplanationPopup)**
    - Extracts the logic for displaying the result.
    - Uses IntelliJ's `JBPopupFactory` to present a clean, non-intrusive HTML balloon near the user's caret.

4. **`settings` (PluginSettingsConfigurable)**
    - Avoids hardcoding API keys.
    - Uses IntelliJ's `PropertiesComponent` to securely save the user's Groq API key in the IDE settings (`Settings -> Tools -> AI Code Explainer`).

## 🚀 How to build and test
1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Open the Gradle tool window and reload the project.
4. Run the `Run Plugin` Gradle task (or simply hit the "Play" button in IntelliJ).
5. In the sandbox IDE that opens:
    - Go to `Settings -> Tools -> AI Code Explainer` and paste a free Groq API key.
    - Highlight any code fragment, right-click, and select **"Explain Code with AI"**.