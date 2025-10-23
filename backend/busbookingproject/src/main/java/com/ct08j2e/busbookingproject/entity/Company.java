package com.ct08j2e.busbookingproject.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Companies")
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;
    
    @Column(name = "name", nullable = false, length = 150)
    private String name;
    
    @Column(name = "logo_url", length = 255)
    private String logoUrl;
    
    @Column(name = "hotline", length = 15)
    private String hotline;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('active', 'inactive')")
    private CompanyStatus status = CompanyStatus.active;
    
    @OneToMany(mappedBy = "company")
    private Set<User> users;
    
    @OneToMany(mappedBy = "company")
    private Set<Route> routes;
    
    @OneToMany(mappedBy = "company")
    private Set<Bus> buses;
    
    public Company() {
    }
    
    public Company(Integer companyId, String name, String logoUrl, String hotline, 
                   String description, CompanyStatus status) {
        this.companyId = companyId;
        this.name = name;
        this.logoUrl = logoUrl;
        this.hotline = hotline;
        this.description = description;
        this.status = status;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Alias method for company name
    public String getCompanyName() {
        return name;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public String getHotline() {
        return hotline;
    }
    
    public void setHotline(String hotline) {
        this.hotline = hotline;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CompanyStatus getStatus() {
        return status;
    }
    
    public void setStatus(CompanyStatus status) {
        this.status = status;
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    public Set<Route> getRoutes() {
        return routes;
    }
    
    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
    }
    
    public Set<Bus> getBuses() {
        return buses;
    }
    
    public void setBuses(Set<Bus> buses) {
        this.buses = buses;
    }
    
    public enum CompanyStatus {
        active, inactive
    }
}