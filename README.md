Privilee Website - Test Automation 

## Overview
The Test Automation Framework is meticulously crafted to provide a robust, scalable, and comprehensive solution for automating the testing of the Privilee Web Application. Designed with precision and efficiency in mind, this framework ensures seamless integration, high performance, and extensive coverage, making it an indispensable tool for delivering quality and reliability in every aspect of the web application’s functionality. 

## Table of Contents
- [QA Test Automation Principles](#qatest-automation-principles)
- [Project Structure](#project-structure)
- [Tech Stack](#technology-stack)
- [Automation Framework Objective](#automation-framework-objective)
- [Automated Test Scenarios](#automated-test-scenarios)
- [Setup and Running](#setup-and-running)
- [Future Enhancement](#future-enhancement)

## QA Test Automation Principles
Our test automation framework embodies a comprehensive suite of features, designed to ensure optimal efficiency and reliability in testing the Privilee Web Application. Key features include:
 
    1. Page Object Model (POM)
    2. Reusability utilities
    3. Data-driven testing
    4. Multiple environments
    5. Cross-browser testing
    6. Parallel testing
    7. Retry mechanism
    8. Test Data Generation
    9. Logging
    10.Code quality
    11.CI/CD integration
 
## Project Structure

## Tech Stack
- **Automation Tool:** Playwright
- **Programming Language:** Java
- **Test Runner:** TestNG
- **Test Report:** Extent Report


### Why Playwright and Java?

- **Programming Language Familiarity:** Java is widely used in enterprise environments, making it a preferred choice due to its performance, scalability, and extensive libraries.
- **Extensive Libraries and Frameworks:** Java's extensive ecosystem includes frameworks like TestNG and JUnit for streamlined test execution and assertion handling, alongside build tools such as Apache Maven and Gradle, ensuring efficient automation project management. 
- **Cross-Browser Support:** Playwright offers robust support for testing across different browsers, ensuring comprehensive test coverage.
- **Reliability:** Playwright is known for its reliability in handling complex interactions and scenarios, making it ideal for robust test automation.
- **Modern Automation Capabilities:** Playwright offers modern automation capabilities such as native event handling, intercepting network requests, and taking screenshots. These features enhance the robustness and flexibility of automated tests.
- **Speed:** With Playwright's ability to run tests in parallel, test execution times can be significantly reduced, improving overall efficiency.
- **Community Support:** Playwright has a strong community that actively contributes to its development, ensuring timely updates and support.
- **Maintainability and Scalability:** Java's object-oriented nature and Playwright's API design contribute to creating maintainable and scalable test automation frameworks. This is crucial for long-term project success and reducing maintenance efforts.

Overall, Playwright with Java provides a robust foundation for building reliable, maintainable, and scalable automated tests for web applications.


### Automated Test Scenarios

Detailed test cases for Menu(including all venus), SignUp, Filter, Map Search functionalities have been written to cover maximum possible scenarios. This ensures comprehensive coverage and helps identify candidates for automation. 

#### SignUp:
TS_01_SignUp: Validate signup and membership payment process for one adult user.

TS_02_SignUp: Validate signup and membership payment process for two adult users

TS_03_SignUp: Validate signup and membership payment process for two adults and children.

TS_04_SignUp: Validate signup and membership payment process for two adults with monthly payment option

#### Menu:
TS_01_Menu: Validate that each menu on the application correctly displays the names of all venues associated with it

TS_02_Menu: Validate the correct functionality of menus and the accurate count of venues

TS_03_Menu: Validate the pool and beach page of all images

#### Filter:
TS_01_Filter: Validate the venue by applying filters and it's api returned results on venue list page

TS_02_Filter: Validate the venue by passing the categories data to the endpoint and to the FE (applying the categories through Filters

TS_03_Filter: Validate the clear filter functionality

#### Search:
TS_01_Search: Validate venue search functionality for guest users on the application.

TS_02_Search: Validate venue search functionality by giving user detail on the application.


## Setup and Runner
### Pre-Setup
- Install Java (version 11 or above)
- IDE: Install IntelliJ (Preferred)
- Install Maven
### To Execute  
  - Test Run Cmd in Local
  ```terminal
     mvn clean test -DsuiteXmlFile=path/to/your/testng.xml
     or
     Open to testng.xml from root directory, rigt click on xml file > Run testng.xml
  ```


### To View Report
    open result-artifacts/reports/TestExecutionReport.html

### To View Failed Screeshots
    Navigate to Project folder result-artifacts/reports/screenshots

### Future Enhancement 
- Enhance CI/CD integration with automated triggers.
- Expand cross-mobile browser (webview) testing capabilities.
