package com.sap.cloud.s4hana.tutorial;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.ReadCostCenterDataNamespace.CostCenter;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.ReadCostCenterDataService;

public class GetCostCentersCommand extends ErpCommand<List<CostCenter>>
{
    private static final Logger logger = CloudLoggerFactory.getLogger(GetCostCentersCommand.class);

    protected GetCostCentersCommand( final ErpConfigContext configContext )
    {
        super(
            HystrixUtil
                .getDefaultErpCommandSetter(
                    GetCostCentersCommand.class,
                    HystrixUtil.getDefaultErpCommandProperties().withExecutionTimeoutInMilliseconds(5000))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(20)),
            configContext);
    }

    @Override
    protected List<CostCenter> run()
        throws Exception
    {
        final List<CostCenter> costCenters =
            ReadCostCenterDataService
                .getAllCostCenter()
                .select(
                    CostCenter.COST_CENTER_I_D,
                    CostCenter.STATUS,
                    CostCenter.COMPANY_CODE,
                    CostCenter.CATEGORY,
                    CostCenter.COST_CENTER_DESCRIPTION)
                .execute(getConfigContext());

        return costCenters;
    }

    @Override
    protected List<CostCenter> getFallback()
    {
        return Collections.emptyList();
    }
}
