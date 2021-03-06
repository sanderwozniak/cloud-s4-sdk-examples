package com.sap.cloud.s4hana.tutorial;

import java.util.Collections;
import java.util.List;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.sap.cloud.sdk.cloudplatform.cache.CacheKey;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataExceptionType;
import com.sap.cloud.sdk.s4hana.connectivity.CachingErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.ReadCostCenterDataNamespace.CostCenter;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.ReadCostCenterDataService;

import lombok.NonNull;

public class GetCachedCostCentersCommand extends CachingErpCommand<List<CostCenter>>
{
    private static final Cache<CacheKey, List<CostCenter>> cache = CacheBuilder.newBuilder().build();

    public GetCachedCostCentersCommand( @NonNull final ErpConfigContext configContext )
    {
        super(GetCachedCostCentersCommand.class, configContext);
    }

    @Override
    protected Cache<CacheKey, List<CostCenter>> getCache()
    {
        return cache;
    }

    @Override
    protected List<CostCenter> runCacheable()
        throws ODataException
    {
        try {
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
        catch( final Exception e ) {
            throw new ODataException(ODataExceptionType.OTHER, "Failed to get CostCenters from OData command.", e);
        }
    }

    @Override
    protected List<CostCenter> getFallback()
    {
        return Collections.emptyList();
    }
}
