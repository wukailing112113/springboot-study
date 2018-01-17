package springboot.pay.alipay.http;

public class ReqHeader {

	private Byte channel;
	private Integer industry;
	private Byte platform;
	private String indentity;
	private String trackId ;

	public final static String CHANNEL = "channel" ;
	public final static String INDUSTRY = "industry" ;
	public final static String PLATFORM = "platform" ;
	public final static String INDENTITY = "indentity" ;
	public final static String TACKID = "trackId" ;


	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public Byte getChannel() {
		return channel;
	}

	public void setChannel(Byte channel) {
		this.channel = channel;
	}

	public Byte getPlatform() {
		return platform;
	}

	public void setPlatform(Byte platform) {
		this.platform = platform;
	}

	public String getIndentity() {
		return indentity;
	}

	public void setIndentity(String indentity) {
		this.indentity = indentity;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

}
