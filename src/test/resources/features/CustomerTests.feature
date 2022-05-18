Feature: Customer CRUD tests
  Scenario: Check customers have only one address
    Given I create 3 customers with all mandatory fields
    Then I check that there are no customers without address

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

  Scenario: Check that random customers have all mandatory fields filled
    Given I create 5 customers with all mandatory fields
    When I get random 2 customers
    Then I check that 2 customers mandatory fields are not empty

  Scenario: Check that customer address cannot be created without mandatory fields
    Given I create 1 customer address without city
    And I cannot save customer address without city
    When I create 1 customer address without country
    And I cannot save customer address without country
    When I create 1 customer address without postal_code
    Then I cannot save customer address without postal_code


  Scenario: Check that random customers orders have required fields
    Given I create 5 customers with all mandatory fields
    And I create 5 orders for created customers
    When I get random 3 customers
    And I get orders for selected users
    Then i verify that all mandatory fields are not null
