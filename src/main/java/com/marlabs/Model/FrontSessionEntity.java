package com.marlabs.Model;

/**
 * 前端用户session信息实体
 * @author shamee-loop
 */
public class FrontSessionEntity extends BaseDomain{

    private User user;// 会员

    private Customer customer;//企业

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
