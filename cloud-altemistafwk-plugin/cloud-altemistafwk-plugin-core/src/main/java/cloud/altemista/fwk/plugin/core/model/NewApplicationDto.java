package cloud.altemista.fwk.plugin.core.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The TSF+ application that is being created
 * @author NTT DATA
 */
public class NewApplicationDto extends ApplicationDto {

	/** The serialVersionUID */
	private static final long serialVersionUID = 414699415911851550L;
	
	/** The type of aggregator project (e.g.: webapp) */
	private String aggregatorType;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("aggregatorType", this.aggregatorType);
	}

	/**
	 * @return the aggregatorType
	 */
	public String getAggregatorType() {
		return aggregatorType;
	}

	/**
	 * @param aggregatorType the aggregatorType to set
	 */
	public void setAggregatorType(String aggregatorType) {
		this.aggregatorType = aggregatorType;
	}
}
