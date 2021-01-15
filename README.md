# DelegationCalculator
Delegation calculator created as exercise for Java classes on Jagiellonian University. Includes date and time management.

## Parameters:  
* *start* - begin of the delegation in format: *yyyy-mm-dd HH:MM timezone*  
* *end* - end of the delegation in format: *yyyy-mm-dd HH:MM timezone*  
* *dailyRate* - delegation daily rate    
  
## Calculating rules:  
* for full day: *dailyRate*  
* to 8 hours: *dailyRate* / 3  
* from 8 to 12 hours: *dailyRate* / 2  
* over 12 hours: *dailyRate*
