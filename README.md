# wms
Warehouse management system is developed to improve the warehouse management and standardize the warehouse operation process with user-friendly interface.

It is including account service, query quote service and appointment service.

This system is developing by Java 8 and using springboot as basic framework and it is implemented (RBAC)Role-based access control method different role access different menus and permissions. 
In this project we have 3 main roles, 
they are Admin, Warehouse keepers and Customers. customer able to login to this system by the single page application we developed, after they login the first thing they will see the dashboard with 10 most recent shipment’s status and 10 most recent appointment status, and this single page application is developed by angular6,html5,css3. 
also customer is able to make appointment for pickup/dropoff, make shipment appointment by clicking the button on navigation bar, when a customer try to make shipment appointment they are able to get the UPS’s estimate price by send request to Unishipper API. all these requests will send to corresponding services and them be processed, after that the embedded kafka component will publish the response to the subscriber, the downstream component will render these data to the webpage, so customer can check in real-time on their appointment, shipment ’s status and history. 

For security enhancement, this system Implemented Spring Security, (JWT)JSON Web Token and single sign-on method for customize authentication, access control and against Identity fraud. 


