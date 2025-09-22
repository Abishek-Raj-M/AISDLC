### Role
You are a **Helpful Agent**! STRICTLY follow the instructions step-by-step to ensure a smooth workflow.

### General Instructions:
- Follow the workflow instructions below meticulously.
- Ensure each step is completed and approved by the user before proceeding to the next step.
- Execute the Workflow Instructions in the exact same order as mentioned below (1, 2, 3, 4, 5).
- For Develop Application Code step, always use Java for backend code and simple HTML/CSS Javascript for frontend code.
- For Develop Application Code step, generate the application only after the generation and user accepts the `application-development.mdc` file.

## Workflow Instructions

### 1. **Create User Stories**
- Generate user stories by reading the requirement document 'Requirements_Simple_Digital_Bookstore.txt' from src/main/resources folder.
- Display **the same output** generated to the user in Chat Window. Do not add any other content.
- Save **the same output** generated in a file named `user-story-generator.mdc` under the src/main/resources folder.

### 2. **Develop Application Code**
- Read the `user-story-generator.mdc` file from src/main/resources folder.
- Based on the user stories, create a file named `dev-application-development.mdc` under the src/main/resources folder. This file should outline the steps and details for developing the application using SpringBoot for backend restful services and simple HTML/CSS for the frontend.
- Once the user accepts the `dev-application-development.mdc` file, use it as a reference to generate the web application code.

### 3. **Develop Unit Tests**
- Create a new file named `dev-unit-testcase-guidelines.mdc` under the src/main/resources folder. This file should provide detailed guidelines for generating unit test cases for the web application.
- Once the user accepts the `dev-unit-testcase-guidelines.mdc` file, use it to develop the unit test cases.
- Execute the unit tests to ensure the application code meets the required standards.


### 4. **Perform Code Review**
- Review the application code by referring to the relevant files created under src folder.
- Create a new file named `dev-code-review.mdc` under the src/main/resources folder.
- Provide feedback or suggestions for improvement based on the review.