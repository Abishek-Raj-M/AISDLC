## Role
You are a helpful and intelligent assistant! Please follow the instructions step by step to complete the tasks effectively.

### General Instructions:
- Follow the workflow instructions below meticulously.
- Ensure each step is completed and approved by the user before proceeding to the next.
- Execute the Workflow Instructions in the exact same order as mentioned below (1, 2, 3).
- For Develop Test Automation Scripts step, ensure that the test automation scripts are developed only after the user accepts the `qa-test-case-generator.md` file.
- For Perform Code Review step, ensure that the code review is done only after the user accepts the `qa-test-automation-development.md` file and generates test automation code.
- Maintain clarity and precision in all `.md` files to facilitate seamless collaboration and execution.
---

## Instructions with a Query

### 1. **Generate Test Cases**
- Fetch all 10 user stories from `user-story-generator.txt` file under the src/main/resources folder. 'src/main/resources/user-story-generator.txt'
- Generate the test cases in BDD Gherkin format.
- Save the generated test cases in a new file named `qa-test-case-generator.md` under the src/main/resources folder.

### 2. **Develop Test Automation Scripts**
- Read the `qa-test-case-generator.md` file to understand the test cases.
- Create a new file named `qa-test-automation-development.md` under the src/main/resources folder to document the process of developing test automation scripts.
- The automation scripts must be generated under `src/test/automation` using **Selenium, Java, and BDD Cucumber** only. Do not use any other language or framework.
- **Application URL Configuration:**
  - The application runs locally at: `http://localhost:8081/`
  - The homepage/landing page URL is: `http://localhost:8081/#`
  - Configure WebDriver to navigate to these URLs for testing
- **Use reliable locators from the application's HTML and CSS files located in `src/main/resources/static/`:**
  - Reference the `index.html` file for element IDs and structure
  - Reference the `style.css` file for CSS class names and selectors
  - Prefer ID-based locators (e.g., `#search-input`, `#cart-count`, `#checkout-btn`) over XPath when possible
  - Use CSS class-based locators (e.g., `.book-card`, `.btn-primary`, `.nav-links`) for dynamic elements
  - Ensure locators are stable and maintainable by using the existing HTML structure and CSS classes
- Write the test automation scripts based on the test cases and document them in the `qa-test-automation-development.md` file.

### 3. **Perform Code Review**
- Review the test automation scripts documented in the `qa-test-automation-development.md` file.
- Provide feedback and suggestions for improvement in a separate file named `qa-code-review.md` under the src/main/resources folder for detailed review comments.