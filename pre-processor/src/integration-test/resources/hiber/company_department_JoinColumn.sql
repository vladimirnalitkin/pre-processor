/*
public class Company {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "DEPARTMENT_M_ID")
    private List<Department> departments_t;
*/

select company0_.id as id1_1_, company0_.prbr_id as prbr_id2_1_, company0_.name as name3_1_ from company company0_

select department0_.department_m_id as departme5_2_0_, department0_.id as id1_2_0_, department0_.id as id1_2_1_,
department0_.prbr_id as prbr_id2_2_1_, department0_.company_id as company_4_2_1_, department0_.name as name3_2_1_
  from department department0_
     where department0_.department_m_id=?