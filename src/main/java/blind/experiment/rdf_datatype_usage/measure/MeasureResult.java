package blind.experiment.rdf_datatype_usage.measure;

/**
 * Represents the result of a measure
 * 
 * <p>
 * Used for inserting the results into the database.
 * </p>
 * <p>
 * Represents a single entry
 * </p>
 */
public class MeasureResult {

	/**
	 * Name of the measure
	 */
	private String measure;
	private String property;
	private String datatype;
	private Long quantity;

	public MeasureResult(String measure, String property, String datatype, Long quantity) {
		this.measure = measure;
		this.property = property;
		this.datatype = datatype;
		this.quantity = quantity;
	}

	public String getMeasure() {
		return measure;
	}

	public String getProperty() {
		return property;
	}

	public String getDatatype() {
		return datatype;
	}

	public Long getQuantity() {
		return quantity;
	}

}
