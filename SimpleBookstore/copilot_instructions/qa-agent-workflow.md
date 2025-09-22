## Role
You are a helpful and intelligent assistant! Please follow the instructions step by step to complete the tasks effectively.

### General Instructions:
- Follow the workflow instructions below meticulously.
- Ensure each step is completed and approved by the user before proceeding to the next.
- Execute the Workflow Instructions in the exact same order as mentioned below (1, 2, 3).
- For Develop Test Automation Scripts step, ensure that the test automation scripts are developed only after the user accepts the `qa-test-case-generator.mdc` file.
- For Perform Code Review step, ensure that the code review is done only after the user accepts the `qa-test-automation-development.mdc` file and generates test automation code.
- Maintain clarity and precision in all `.mdc` files to facilitate seamless collaboration and execution.
---

## Instructions with a Query

### 1. **Generate Test Cases**
- Fetch all 15 user stories from `user-story-generator.mdc` file under the src/main/resources folder.
- Generate the test cases in BDD Gherkin format.
- Save the generated test cases in a new file named `qa-test-case-generator.mdc` under the src/main/resources folder.

### 2. **Develop Test Automation Scripts**
- Read the `qa-test-case-generator.mdc` file to understand the test cases.
- Create a new file named `qa-test-automation-development.mdc` under the src/main/resources folder to document the process of developing test automation scripts.
- The automation scripts must be generated under `src/test/automation` using **Selenium, Java, and BDD Cucumber** only. Do not use any other language or framework.
- Write the test automation scripts based on the test cases and document them in the `qa-test-automation-development.mdc` file.

### 3. **Perform Code Review**
- Review the test automation scripts documented in the `qa-test-automation-development.mdc` file.
- Provide feedback and suggestions for improvement in a separate file named `qa-code-review.mdc` under the src/main/resources folder for detailed review comments.