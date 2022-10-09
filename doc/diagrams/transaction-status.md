### Transaction Statuses  
Each transaction in our project consists of these statuses:
* **RECIEVED** : It's a state when the request has reached to the server and is the beginning of procces.
* **PENDING** : Suddenly after starting to process the request, it's status changes to pending status.
* **CONFIRMED** : After checking conditions and if a blockchain transaction is done, the transaction converts to success.
* **SUCCESS** : It's the last step which the balance of the user has been altered.
* **REVERTED** : When due to some problems or a bad request, transaction is denied.