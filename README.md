# TestLink Brief

TestLink is a web-based test management system that facilitates software quality assurance. It is developed and maintained by Teamtest. The platform offers support for test cases, test suites, test plans, test projects and user management, as well as various reports and statistics.

#### Features
1. user roles and management
2. grouping of test cases in test specifications
3. test plans

The basic units used by TestLink are: Test Case, Test Suite, Test Plan, Test Project and User.

##### Test Plan
Test Plans are the basic unit for executing a set of tests on an application. Test Plans include Builds, Milestones, User assignment and Test Results.

##### Test Case
A Test Case describes a simple task in the workflow of an application. A test case is a fundamental part of TestLink. After a tester runs a test case it can either pass, fail or block it. Test cases are organized in test suites.

##### Test Projects
Test Projects are the basic organisational unit of TestLink. Test Projects could be products or solutions of your company that may change their features and functionality over time but for the most part remains the same. 

##### Test Specifications
TestLink breaks down the Test Specification structure into Test Suites and Test Cases. These levels are persisted throughout the application. One Test Project has just one Test Specification.

# TestLinkImportModule

As TestLink is a platform which offers support for test suites etc.

1. In order to write test cases inside testsuite manually, we can write test cases inside test suites in Excel Sheet.
2. These Excel Files then will be converted into XML file using java program.
3. In TestLink under Test Specification, we can import xml generated using Java Program and upload it in testLink to import the test suite. 
4. A Java Maven Project to convert Excel file to Xml File and use the same in TestLink.
5. The Idea is to upload the Xml file on TestLink import functionality for test Suites.
