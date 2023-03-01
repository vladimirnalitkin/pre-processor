/*
public class Company {
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Department.class , mappedBy ="company" ,fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "DEPARTMENT_M_ID")
     private List<Department> departments_t;
*/


org.hibernate.AnnotationException: Associations marked as mappedBy must not define database mappings like @JoinTable or @JoinColumn: com.example.demo.model.Company.departments_t
