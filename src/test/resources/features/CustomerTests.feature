Feature: Customer CRUD tests
  Scenario: Check customers have only one address
    Given I create 3 customers with all mandatory fields
    Then I check that  there are no customers without address

  Scenario: Check that customers don't have same phone and email
    Given I create 3 customers with all mandatory fields
    Then I compare customers phone and email them that are not equal

  Scenario: Check that customer can be create with all mandatory fields
    Given I create 1 customer with all mandatory fields
    Then I verify that customer is created correctly

  Scenario: Check that customer cannot be created without mandatory fields
    Given I create 1 customer without mandatory fields name
    And I cannot save the customer without name
    Given I create 1 customer without mandatory fields address_id
    Then I cannot save the customer without address_id