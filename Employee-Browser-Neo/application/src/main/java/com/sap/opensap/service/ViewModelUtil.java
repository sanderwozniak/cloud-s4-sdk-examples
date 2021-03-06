package com.sap.opensap.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;

import com.sap.cloud.sdk.s4hana.serialization.CostCenter;
import com.sap.opensap.datamodel.s4.CostCenterDetails;
import com.sap.opensap.datamodel.successfactors.EmployeeDetails;
import com.sap.opensap.datamodel.successfactors.JobDetails;
import com.sap.opensap.generated.model.VmCostCenter;
import com.sap.opensap.generated.model.VmEmployee;

public class ViewModelUtil
{
    static VmCostCenter toVmCostCenter(final CostCenterDetails costCenter, final Collection<VmEmployee> vmEmployees)
    {
        return new VmCostCenter()
                .withCompanyCode(costCenter.getCompanyCode().toString())
                .withCostCenter(costCenter.getId().toString())
                .withDescription(costCenter.getDescription())
                .withEmployees(Lists.newArrayList(vmEmployees));
    }

    static VmEmployee toVmEmployee(final EmployeeDetails employee)
    {
        JobDetails details = employee.getDetails();
        return new VmEmployee()
                .withPersonIdExternal(employee.getPersonIdExternal())
                .withDepartment(details.getDepartment())
                .withDivision(details.getDivision())
                .withEmploymentType(details.getEmploymentType())
                .withJobTitle(details.getJobTitle())
                .withPayGrade(details.getPayGrade());
    }

    static List<VmCostCenter> toVmCostCenters(List<CostCenterDetails> costs, Multimap<CostCenter, VmEmployee> employees)
    {
        List<VmCostCenter> result = Lists.newLinkedList();
        for(CostCenterDetails costCenter : costs) {
            final Collection<VmEmployee> employeesInCostCenter = employees.get(costCenter.getId());
            if(!employeesInCostCenter.isEmpty()) {
                result.add(ViewModelUtil.toVmCostCenter(costCenter, employeesInCostCenter));
            }
        }
        return result;
    }

    static Multimap<CostCenter, VmEmployee> toVmEmployeesMap(final List<EmployeeDetails> employees)
    {
        Multimap<CostCenter, VmEmployee> employeesInCostCenter = HashMultimap.create();
        for(EmployeeDetails employee : employees) {
            if(employee.hasDetails()) {
                employeesInCostCenter.put(employee.getDetails().getCostCenter(), ViewModelUtil.toVmEmployee(employee));
            }
        }
        return employeesInCostCenter;
    }
}
