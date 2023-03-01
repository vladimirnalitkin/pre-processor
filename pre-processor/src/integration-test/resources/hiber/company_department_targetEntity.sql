/*
public class Company {
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Department.class ,fetch = FetchType.LAZY, orphanRemoval = true)
     private List<Department> departments_t;
*/

select company0_.id as id1_1_, company0_.prbr_id as prbr_id2_1_, company0_.name as name3_1_ from company company0_

select department0_.company_id as company_1_2_0_, department0_.departments_t_id as departme2_2_0_,
department1_.id as id1_3_1_, department1_.prbr_id as prbr_id2_3_1_, department1_.company_id as company_4_3_1_,
department1_.name as name3_3_1_
from company_departments_t department0_
    inner join department department1_
        on department0_.departments_t_id=department1_.id
            where department0_.company_id=?