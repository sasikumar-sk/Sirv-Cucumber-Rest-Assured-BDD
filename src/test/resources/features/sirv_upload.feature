Feature: Upload a file to Sirv and clean up

  Scenario: Upload, verify via stat, and delete
    Given I have a valid Sirv bearer token
    When I upload the local file to the configured Sirv path
    Then the file should exist on Sirv with a positive size
    And I delete the file from Sirv
