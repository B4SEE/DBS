# Semester project for DBS (Database systems) summer course 2023/2024

## Status: In development :construction:

---

&nbsp;

## 🔎 About

JPA application base for the semester project for the Database Systems course. 
The project is based on the requirements of the course and is designed to demonstrate the knowledge of the students in the field of database systems.

&nbsp;

## 🚩 Checkpoints

<details><summary>Click to expand</summary>
&nbsp;
  
<details><summary>:heavy_check_mark: CP-0 Topic selection</summary>
&nbsp;

- Use a few sentences to describe the topic of your work. Briefly describe the motivation for your chosen topic.

- Consider a topic that includes both a (taxonomic) description of the system and operational data (e.g. measurements, periodic realization, etc.).

- Choose a topic that you can implement within your current knowledge to meet the requirements below. In case of uncertainty, contact the trainer.

- The topic within the group must be unique, not identical to the sample topics discussed in the tutorials and lectures.
  
</details>

---

<details><summary>:heavy_check_mark: CP-1 Conceptual model</summary>
&nbsp;

- Use ER Dia to create a relational conceptual model and submit it as an archive containing:
  - XML document cp0-krm.xml;
  - PDF document cp0-krm.pdf containing an image and a short description of the model/technical justification of the construct used;
    
- The conceptual model must contain at least 5 entity types, the number of entity types should not exceed 10.

- Do not introduce any artificial identifiers for the purpose of the conceptual model.

- Based on technical reasons, design a conceptual model that contains at least once:
  - A single, structured and multiple attribute;
  - 1:1, 1:N and N:M binding using cardinalities 0..1, 1..1, 0..N, 1..N;
  - Recursive or reflexive binding;
  - Inheritance;
  - Weak entity type;

- Specify relevant identifiers in the model, introducing at least one entity type (failure to specify a relevant identifier is considered an error):
  - Composite identifier;
  - Multiple identifiers;
  
</details>

---

<details><summary>:heavy_check_mark: CP-2 Relational model</summary>
&nbsp;

- Transform the conceptual model (CP-1) into a relational model in text form, submit as an archive containing:
  - cp1-krm.png image containing the (possibly corrected) conceptual model;
  - XML document cp1-krm.xml containing the conceptual model;
  - HTML document cp1-krm.html containing the relational model;
    
- If you deem it appropriate, add new entities/relationships/characteristics to the previous conceptual model.

- Use the text notation Table (Key, Attribute1, Attribute2), list foreign keys. Do not consider NULL values.
  
</details>

---

<details><summary>:heavy_check_mark: CP-3 SQL - Database creation, data queries</summary>
&nbsp;

Transform the relational model (CP-2) into an ER model and the resulting SQL queries creating a database, formulate queries on the data over this database, submit as a PDF document containing:

- Figure - ER model and relational model.
  
- SQL queries to create the database including adequate integrity constraints, in particular the introduction of:
  - Adequate types for each attribute;
  - Attribute integrity constraints;
  - A table integrity constraint;
  - Foreign keys including (technically justified) ON UPDATE/DELETE directive;
  - Keys and primary keys, or introduce artificial keys if appropriate;
    
- SQL queries to retrieve data from the database covering at least once:
  - External joins of tables;
  - Internal table joins;
  - A condition on the data;
  - Aggregation and a condition on the value of the aggregation function;
  - Sorting and paging;
  - Set operations;
  - Nested SELECT;
    
- For each query, describe its operation in words, specify the query, and take a screenshot of the query result that is returned by the client.
  
- For the purposes of this task, it is imperative to.
  
- Create all tables in your student database on the server.

- All SQL queries must be (error-free) executable on that server.

- Populate the created tables with the relevant number of data.

- Fill one table (with key relevance for the topic being processed) with a larger amount (~32k) of "operational" data.

- Do not use ALTER TABLE to add integrity constraints.

- Recommended tools:
  - MicroOLAP for Postgresql;
  - DB Designer;
  
</details>

---

<details><summary>:heavy_check_mark: CP-4 Advanced Database Technologies</summary>
&nbsp;

Extend the CP-3 database with other advanced technologies, include a brief comment and necessary SQL queries in the PDF document.

As part of this submission, demonstrate:

- Calling the transaction and query set including setting the appropriate isolation level, indicate the conflict that could arise if the transaction were not used.

- Create and use a view.

- Creating and using a trigger.

- Create and use an index, discussing the benefits of using such an index with the help of relevant analysis.
  
</details>

</details>

&nbsp;

## 📋 Requirements/Necessary features for project

<details><summary>Click to expand</summary>
&nbsp;

- :construction: A data model corresponding to the entire database including:
  - Many To Many binding
  - Inheritance
   
- :construction: DAO layer providing the necessary low-level access to the data including parameterized query
  
- :construction: A service layer calling the DAO layer containing 5 selected uses of your data, specialized mainly for write operations and covering the transaction from CP-4.

</details>

&nbsp;

## 📦 Installation

<details><summary>Click to expand</summary>
&nbsp;

❗ **Please always pull the latest changes before running the project.** ❗

There is no .jar file available for this project yet. You can clone the repository and run the project locally.

Steps to clone the repository:

1. Open the terminal and navigate to the directory where you want to clone the repository.

2. Run the following commands:
   ```bash
    git clone https://github.com/B4SEE/DBS.git
    ```

3. Create `user_credentials/` directory in the `dbs/` directory and add the `credentials.txt` file with the following content:
   ```
    hostname
    username
    password
    ```
    Replace `hostname`, `username`, and `password` with your database credentials.

4. Open the project in your IDE and navigate to the `dbs/src/main/java/cs/cvut/fel/dbs/Main.java` file.

5. Click on Run > Edit Configurations and set environment variables for the database connection.

6. Run the project.

</details>

&nbsp;

## 📞 Contact

<details><summary>Click to expand</summary>

### **Author** - Eleonora Virych

📧 Email: [virichelia@gmail.com](mailto:virichelia@gmail.com)

</details>
