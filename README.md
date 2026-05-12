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

## 🚀 How to Build and Run

> ⚠️ **Java 21 Required:** This project is built against **Java 21 (JBR 21)**. If you are building via CLI, ensure your `JAVA_HOME` does not point to experimental versions (e.g., Java 25), as Gradle may fail to initialize the daemon.

**Option 1: Inside IntelliJ IDEA (Recommended)**
1. Clone the repository and open the project folder in IntelliJ IDEA.
2. Wait for Gradle to sync the project.
3. Execute the `Run Plugin` run configuration (or click the green Play button in the top right corner).

**Option 2: Via Terminal**
1. Clone the repository and navigate to the project root directory.
2. Ensure your terminal's Java version is set to 21. For example, in PowerShell:
   ```powershell
   $env:JAVA_HOME="C:\path\to\your\java-21"
   ```
3. Execute the Gradle wrapper task depending on your OS:

   **Windows (PowerShell):**
   ```powershell
   .\gradlew runIde
   ```

   **macOS / Linux:**
   ```bash
   ./gradlew runIde
   ```

## 💡 Usage Instructions
1. Once the Sandbox IDE launches, navigate to **Settings -> Tools -> AI Code Explainer**.
2. Paste a free Groq API Key.
3. Open any source file, highlight a snippet of code, right-click, and select **"Explain Code with AI"**.