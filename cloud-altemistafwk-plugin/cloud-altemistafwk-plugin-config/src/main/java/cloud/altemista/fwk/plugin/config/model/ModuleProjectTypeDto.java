/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Module project type (e.g.: common, core, web)
 * @author NTT DATA
 */
public class ModuleProjectTypeDto extends OptionWithArtifactDto {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 6933976580005510639L;

	/** Are projects of this type aggregator projects? (e.g.: webapp) */
	private boolean aggregator;
	
	/**
	 * Priority to consider module project of this type a candidate to have the shared configurations.
	 * Lower values (starting with 0) represent higher priority.
	 * {@code null} values mean this module project type should not be considered as a candidate
	 * @see cloud.altemista.fwk.plugin.core.impl.PluginReaderServiceImpl#computeSharedImplementationPriority(ModuleProjectDto)
	 * @see cloud.altemista.fwk.plugin.config.Constant#SHARED_IMPLEMENTATION_PRIORITY_BY_SUFFIX
	 */
	@SuppressWarnings("javadoc")
	private Integer sharedImplementationPriority;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionWithArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("aggregator", this.aggregator) //$NON-NLS-1$
				.append("sharedImplementationPriority", this.sharedImplementationPriority); //$NON-NLS-1$
	}
	
	/**
	 * Duplicated getter (with get* prefix instead of is*) to avoid
	 * <code>YamlReaderException: Unable to find property 'aggregator' on class: o.t.p.p.c.m.FeatureDto</code>
	 * @return the deprecated
	 * @deprecated For YamlReader only. Use {@link #isAggregator()} instead
	 */
	@Deprecated
	public boolean getAggregator() {
		return this.isAggregator();
	}

	/**
	 * @return the aggregator
	 */
	public boolean isAggregator() {
		return aggregator;
	}

	/**
	 * @param aggregator the aggregator to set
	 */
	public void setAggregator(boolean aggregator) {
		this.aggregator = aggregator;
	}

	/**
	 * @return the sharedImplementationPriority
	 */
	public Integer getSharedImplementationPriority() {
		return sharedImplementationPriority;
	}

	/**
	 * @param sharedImplementationPriority the sharedImplementationPriority to set
	 */
	public void setSharedImplementationPriority(Integer sharedImplementationPriority) {
		this.sharedImplementationPriority = sharedImplementationPriority;
	}

}
