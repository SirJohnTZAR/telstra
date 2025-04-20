Feature: Sim card activator
  Activate sim card

Scenario:  Failed activation
  Given iccid fail "8944500102198304826"
  When request to activate sim card
  Then should return activation failed "false"

  Scenario: Successful activation
    Given iccid pass "8944500102198304825"
    When request to activate sim card
    Then should return activation success "true"