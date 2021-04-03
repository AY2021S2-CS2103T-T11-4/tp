---
layout: page
title: User Guide
---

<div class="toc-no-bullet-points">
  * Table of Contents
  {:toc}
</div>

## 1. Introduction
Welcome to our **User Guide** and thank you for using CakeCollate! Are you a home baker searching for a reliable tool to keep track of your orders? 
While there may be other modern applications such as Microsoft Excel that may be able to help you keep track of your data, they may not be the most efficient or simplest
way for you to manage your data. 

CakeCollate promises to be an efficient desktop application that allows you to easily consolidate and manage your orders. Our main features include:<br>
1. Order management
2. Order Item management
3. Reminders for orders that have delivery dates approaching the current date
4. Checking the delivery status of your orders

It is optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). If you're a small-time cake seller that can type fast, CakeCollate can get your order management tasks done faster than traditional GUI apps.

--------------------------------------------------------------------------------------------------------------------

## **3. Quick start**

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `cakecollate.jar` from [here](https://github.com/AY2021S2-CS2103T-T11-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your CakeCollate.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data. 
You can use the sample data pre-loaded in the application to get play around and get used to the available commands. Use the command `clear`
 to remove all sample data when you are used to the commands and want to begin inputting your own data.<br><br>
   ![Ui](images/Ui.png)


1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will display the help window.<br>
   Some example commands you can try:

   * **`list`** : Lists all orders in the CakeCollate database.

   * **`add`**`n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 d/13-05-2021 o/2 x Strawberry Cake` : Adds a `2 x Strawberry Cake` order to CakeCollate.

   * **`delete`**`3` : Deletes the 3rd order shown in the current list.
   
   * **`remind`**`2` : Lists all orders that are 2 days within the current local date.

   * **`clear`** : Deletes all order in the CakeCollate database.

   * **`exit`** : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

<div markdown="span" class="alert alert-info">
**:information_source: You can remove all the sample data using the clear command once you are ready to use CakeCollate.**<br>
</div>

--------------------------------------------------------------------------------------------------------------------

## **4. Features**

<div markdown="block" class="alert alert-info">

**:information_source: How to interpret the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times. <br>  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc. <!-- order desc? --> 

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* If a parameter is expected only once in the command but you specified it multiple times, only the last occurrence of the parameter will be taken.<br>
  e.g. if you specify `p/12341234 p/56785678`, only `p/56785678` will be taken.

* `d/DELIVERY_DATE` should specify a `DELIVERY_DATE` that is valid and is a future date.
  i.e. `DELIVERY_DATE` can be the date today or a date after today.

* `INDEXES` refer to the list number of orders on the left of the GUI while `ORDER_ITEM_INDEXES` refers to the list number of order items on the right of the GUI <!-- [comment]: <> (can add a link to the gui screenshot and annotate which index refers to what) -->

* Items that are `INDEXES` or `ORDER_ITEM_INDEXES` take in whole number parameters separated by spaces. 
  For example, for a command that takes in `oi/ORDER_ITEM_INDEXES`, you can input `oi/1` or `oi/1 4 5`.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

</div>

### Viewing help : `help`

* Gives instructions on how to enter orders into the CLI.
* Displays a message with a list of all available commands and their format.
* To return to the main order list click on the `Return to the main order list` button. 
* Help can also be accessed by clicking the `help` button in the top left corner of the application or by clicking the `F1` keyboard key.

Format: `help`

### Adding an order: `add`

Adds an order to the CakeCollate database. The order will be initialised as undelivered, but can be modified with the `delivered` and `cancelled` commands found below.

#### Simple format

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS d/DELIVERY_DATE o/ORDER_DESCRIPTION... [t/TAG]...`

Examples:

* `add n/Betsy Crowe e/betsycrowe@example.com a/Newgate Prison p/1234567 d/13-05-2100 o/chocolate cake o/5 durian cake o/10 mochi cake 100 t/friend t/daughter` adds an order with all compulsory fields, three order descriptions, and a friend and daughter tag.

* `add n/Betsy Crowe e/betsycrowe@example.com a/Newgate Prison p/1234567 d/13-05-2100 o/chocolate cake` adds an order with all compulsory fields, one order description and no tags.


#### Alternative format 

Advanced Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS d/DELIVERY_DATE oi/ORDER_ITEM_INDEXES [o/ORDER_DESCRIPTION]... [t/TAG]...`

* The difference here compared to the previous format is that you can omit the `ORDER_DESCRIPTION` parameter is optional, but you need to include an `ORDER_ITEM_INDEXES` parameter.

* This alternative format is aimed at saving you some typing. If an order description you want to type already exists in the order items table of the GUI, you can specify its index instead of typing its entire name out.

* For example, for an order that involves chocolate cake, if `chocolate cake` exists in the order items table as shown in the screenshot below, instead of typing `o/chocolate cake`, you can type out `oi/1`

[comment]: <> ({screenshot})

Examples

* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 d/13-05-2021 oi/1 4 5` adds an order with all compulsory fields and adds order items 1, 4, 5 of order items table to the order.

* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 d/13-05-2021 o/strawberry cake oi/1` adds an order with all compulsory fields, an order description of strawberry cake and the first item in the list of order items in the GUI.

:information_source: **How are items added to the order items table?**<br>

When you specify an order description using the prefix `o/`, e.g. `o/chocolate cake`, the app automatically adds it into the order items table. As such, you don't need to go through extra steps to add in items into the order items table manually.

### List all existing orders : `list`

Shows a list of all orders in the CakeCollate database.

Format: `list`

### Editing an order : `edit`

Edits an existing order in the CakeCollate database.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG] [d/DELIVERY_DATE] [o/ORDER_DESCRIPTION]​`

* Edits the order at the specified `INDEX`. The index refers to the index number shown in the displayed order list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the order will be removed i.e adding of tags is not cumulative.
* You can remove all the order’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st order to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd order to be `Betsy Crower` and clears all existing tags.

### Locating orders by name: `find`

Find orders whose specified field contain any of the given keywords.

Format: `find [n/KEYWORD_NAME]... [p/KEYWORD_PHONE]... [e/KEYWORD_EMAIL]... [a/KEYWORD_ADDRESS]... [o/KEYWORD_ORDER_DESCRIPTION]... [t/KEYWORD_TAG]... [d/KEYWORD_DELIVERY_DATE]... [s/KEYWORD_DELIVERY_STATUS]... [r/KEYWORD_REQUEST]... `

* The search is case-insensitive. e.g `hans` will match `Hans`.
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`.
* Fields are searched according to specified prefixes. e.g. `n/Hans` will only find orders with name that match `Hans`.
* Sub-strings will be matched e.g. `Han` will match `Hans`.
* If no prefixes are specified 
* If no prefixes are specified, orders matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`.
* If multiple keywords are specified for a certain prefix, orders matching at least one keyword for the speficied field will be returned. (i.e. `OR` search). e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`.
* If multiple prefixes are specified, each keyword specified for each field must match orders with corresponding fields. (i.e. `AND` search) e.g. `n/Hans o/Cake` will only match orders with name that matches `Hans` and order description that matches `Cake`.
* `AND` searches will take priority.

Examples:
* `find n/John` will return all orders with name `john`, `John Doe` or `Johnathan`.
* `find n/Alex Bob` will return all orders with name `Alex`, `alexander`, `Bob` or `bobby`.
* `find n/Alex o/Chocolate` will return all orders with name `Alex` and order description `Chocolate`. <br>
  ![result for 'find n/Alex o/Chocolate'](images/findAlexChocolate.PNG) <br>
* `find alex bernice` will return all orders that matches `alex` or `bernice`, <br>
  ![result for 'find alex bernice'](images/findAlexBernice.PNG) <br>
* `find n/Alex Charlotte o/Chocolate` will return all orders that matches (`Alex` or `Charlotte`) and `Chocolate`. <br>
  ![result for 'find n/Alex Charlotte o/Chocolate'](images/findAlexCharlotteChocolate.PNG) <br>
  

### Deleting an order : `delete`

Deletes the specified orders from the CakeCollate database.

Format: `delete INDEXES`

* Deletes the orders with the specified `INDEXES`.
* The `INDEXES` refers to the order `INDEXES` number shown in the list of orders.
* One or more `INDEXES` can be entered.
* The `INDEXES` **must be valid**.

Examples:
* `delete 2` deletes the order with `INDEX` 2 from the CakeCollate database.
* `delete 2 3` deletes the orders with `INDEXES` 2 and 3 from the CakeCollate database.

### Receiving reminders for orders : `remind`

Displays a list of reminder for orders that are X days within the current date.

Format: `remind DAYS`

* Lists all orders within the current date to the numbers of days from the specified date.
* The `DAYS` refers to the number of days from the current date.
* The `DAYS` **must be a positive integer starting from 0**.

Examples:
* `remind 0` lists all orders that have a delivery date for today.
* `remind 3` lists all orders that have a delivery date within 3 days from today.

### Adding a special request to an order: `request`

Adds a special request to an existing order in the CakeCollate database.

Format: `request INDEX r/REQUEST`

* Adds a special request to the order at the specified `INDEX`. The index refers to the index number shown in the displayed order list. The index **must be a positive integer** 1, 2, 3, …​
* You can remove an order’s special request by typing `r/` without specifying any requests after it.

Examples:
* `request 1 r/More sugar, spice and everything nice.` Sets the special request of the 1st order to be `More sugar, spice and everything nice.`
* `request 2 r/` Removes the 2nd order's current special request.


### Setting the delivery status of an order as undelivered : `undelivered`

Sets the delivery status of the specified order/orders from the CakeCollate database as `undelivered`.

Format: `undelivered ID...`

* Sets the delivery status of the orders with the specified ids as `undelivered`.
* The `ID` refers to the order `ID` number shown in the list of orders.
* One or more ids can be entered.
* The `ID` **must be valid**.

Examples:
* `list` followed by `undelivered 2` sets the delivery status of the orders with `ID` 2 from the 
  CakeCollate database as `undelivered`.
* `undelivered 2 3` sets the delivery status of the orders with ids 2 and 3 from the 
  CakeCollate database as `undelivered`.

### Setting the delivery status of an order as delivered : `delivered`

Sets the delivery status of the specified order/orders from the CakeCollate database as `delivered`.

Format: `delivered ID...`

* Sets the delivery status of the orders with the specified ids as `delivered`.
* The `ID` refers to the order `ID` number shown in the list of orders.
* One or more ids can be entered.
* The `ID` **must be valid**.

Examples:
* `list` followed by `delivered 2` sets the delivery status of the orders with `ID` 2 from the
  CakeCollate database as `delivered`.
* `delivered 2 3` sets the delivery status of the orders with ids 2 and 3 from the CakeCollate database as `delivered`.

### Setting the delivery status of an order as cancelled : `cancelled`

Sets the delivery status of the specified order/orders from the CakeCollate database as `cancelled`.

Format: `cancelled ID...`

* Sets the delivery status of the orders with the specified ids as cancelled.
* The `ID` refers to the order `ID` number shown in the list of orders.
* One or more ids can be entered.
* The `ID` **must be valid**.

Examples:
* `list` followed by `cancelled 2` sets the delivery status of the orders with `ID` 2 from the
  CakeCollate database as `cancelled`.
* `cancelled 2 3` sets the delivery status of the orders with ids 2 and 3 from the CakeCollate database as `cancelled`.

### Clearing all existing orders: `clear`

Deletes all existing orders in the CakeCollate database.

Warning: this is an undo-able operation.

Format: `clear`

### Adding order items: `addItem`

Adds an order item to the order items table on the right side of the GUI.

Format: `addItem ORDER_ITEM_DESCRIPTION`

* The `ORDER_ITEM_DESCRIPTION` can only contain alphanumeric characters and ".

Examples:
* `addItem 2 x Red Velvet` adds an order item with the specified description to the order items table.

### Deleting order items: `deleteItem`

Deletes order items from the order items table based on the specified list of indices.

Format: `deleteItem ORDER_ITEM_INDEXES`

* The `ORDER_ITEM_INDEX` refers to the `ORDER_ITEM_INDEX` number shown in the order items table.
* One or more `ORDER_ITEM_INDEXES` can be entered.
* The `ORDER_ITEM_INDEXES` **must be valid**.

Examples:
* `deleteItem 2` deletes the order items with `ORDER_ITEM_INDEX` 2 from the order items table.
* `deleteItem 2 3` deletes the order items with `ORDER_ITEM_INDEXES` 2 and 3 from the order items table.

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

CakeCollate data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

CakeCollate data is saved as a JSON file `[JAR file location]/data/cakecollate.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, CakeCollate will discard all data and start with an empty data file at the next run.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

### Keyboard shortcuts you can use

* Click the `up` arrow in the keyboard to traverse up the previously inputted commands if they exist. 
* Click the `down` arrow in the keyboard to traverse down the previously inputted commands if they exist. 
* Click the `Shift`Button followed by the `backspace` button on the keyboard to delete all the text in the conmand box in one go. 

--------------------------------------------------------------------------------------------------------------------

## **5. FAQ**

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous CakeCollate home folder.

--------------------------------------------------------------------------------------------------------------------

## **6. Command summary**

Action  | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS d/DELIVERY_DATE o/ORDER_DESCRIPTION... [t/TAG]...` <br> e.g., `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 d/13-05-2021 o/strawberry cake 3` <br><br> `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS d/DELIVERY_DATE oi/ORDER_ITEM_INDEXES [o/ORDER_DESCRIPTION]... [t/TAG]...` <br> e.g. `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 d/13-05-2021 o/strawberry cake oi/1` <br>
**Clear** | `clear`
**Delete** | `delete INDEXES`<br> e.g., `delete 3 4`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [o/ORDER_DESCRIPTION]... [t/TAG]...`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find [n/KEYWORD_NAME]... [p/KEYWORD_PHONE]... [e/KEYWORD_EMAIL]... [a/KEYWORD_ADDRESS]... [o/KEYWORD_ORDER_DESCRIPTION]... [t/KEYWORD_TAG]... [d/KEYWORD_DELIVERY_DATE]... [s/KEYWORD_DELIVERY_STATUS]... [r/KEYWORD_REQUEST]... `<br> e.g., `find James Jake`, `find n/Alex o/Chocolate`, `find n/Bernice d/march s/undelivered` 
**List** | `list`
**Help** | `help`
**Remind** | `remind DAYS`<br> e.g., `remind 3`
**Request** | `request INDEX [r/REQUEST]` <br> e.g., `request 1 r/More sugar, spice and everything nice.`
**Undelivered** | `undelivered INDEXES`<br> e.g., `undelivered 3 4`
**Delivered** | `delivered INDEXES`<br> e.g., `delivered 3 4`
**Cancelled** | `cancelled INDEXES`<br> e.g., `cancelled 3 4`
**Add Order Item** | `addItem ORDER_ITEM_DESCRIPTION`<br> e.g., `addItem 2 x Red Velvet`
**Delete Order Item** | `deleteItem ORDER_ITEM_INDEXES`<br> e.g., `deleteItem 2 3`

--------------------------------------------------------------------------------------------------------------------

## Acknowledgements

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).