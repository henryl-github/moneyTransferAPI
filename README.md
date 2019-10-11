# moneyTransferAPI

# Datastore
   * BankAccount table - Store bank account information such as accountOwnerName, balance, currency
   * Transfer table - Store all bank transfers. Column fromAccount and toAccount are foreign key to BankAccount table(Data Integrity)
   * User table(not implemented) - Telephone number/email address/passport number can be used as unique identifier. A user can hold multiple bank accounts
   
# Concurrency
   * Locking/synchronization - Maximize concurrency by using database row level locking instead of table locking
   * Race condition - Use database row level locking(write lock) to ensure no race condition, see stress test 
   * Deadlock - Lock rows in increasing order of accountId to ensure no deadlock, see stress test
   
# Libraries used
   * H2 - in memory database to help ensure transaction(ACID) and datastore
   * Jersey -  to create RESTful web service
   * Grizzly - HTTP server to host web service
   * Junit - for Unit test and Integration test
  
# REST 
   API end point http://localhost:8090
   
