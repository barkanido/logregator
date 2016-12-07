package com.tikal.logregator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogEntry {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LogEntry.class);

	private static String regex = "([^ ]*) ([^ ]*) ([^ ]*):([0-9]*) (-|[^ :]*):?([0-9]*)? (-?[.0-9]*) (-?[.0-9]*) (-?[.0-9]*) (-|[0-9]*) (-|[0-9]*) ([-0-9]*) ([-0-9]*) \"([^ ]*) ([^ ]*) (- |[^ ]*)\"(?: \"(.*)\" (-|[^ ]*) (-|[^ ]*))?$";

	private static String urlRegexp = "^http[s]*://(.*):\\d+/(.*)$";

	private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.'SSSSSS'Z'", Locale.US);
	private static final Pattern pattern = Pattern.compile(regex);
	private static final Pattern urlPattern = Pattern.compile(urlRegexp);
//

	@JsonProperty
	private long epochTime;
	@JsonProperty
	private String elbName;
	@JsonProperty
	private String backendIP;
	@JsonProperty
	private  float requestProcessingTimeInSec;
	@JsonProperty
	private  float backendProcessingTimeInSec;
	@JsonProperty
	private int elbHttpErrorCode;
	@JsonProperty
	private int backendHttpErrorCode;
	@JsonProperty
	private String httpMethod;
	@JsonProperty
	private String url;

	@Override
	public String toString() {
		return "LogEntry{" +
				"epochTime=" + epochTime +
				", elbName='" + elbName + '\'' +
				", backendIP='" + backendIP + '\'' +
				", requestProcessingTimeInSec=" + requestProcessingTimeInSec +
				", backendProcessingTimeInSec=" + backendProcessingTimeInSec +
				", elbHttpErrorCode=" + elbHttpErrorCode +
				", backendHttpErrorCode=" + backendHttpErrorCode +
				", httpMethod='" + httpMethod + '\'' +
				", url='" + url + '\'' +
				'}';
	}

	public LogEntry(long epochTime,
					String elbName,
					String backendIP,
					float requestProcessingTimeInSec,
					float backendProcessingTimeInSec,
					int elbHttpErrorCode,
 					int backendHttpErrorCode,
					String httpMethod, String url) {
		this.epochTime = epochTime;
		this.elbName = elbName;
		this.backendIP = backendIP;
		this.requestProcessingTimeInSec = requestProcessingTimeInSec;
		this.backendProcessingTimeInSec = backendProcessingTimeInSec;
		this.elbHttpErrorCode = elbHttpErrorCode;
		this.backendHttpErrorCode = backendHttpErrorCode;
		this.httpMethod = httpMethod;
		this.url = url;
	}

	public static LogEntry parse(final String line) {
		final Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()){
			logger.error("Bad log entry {}",line);
			return null;
		}

		String url = matcher.group(15);
		Matcher urlMatcher = urlPattern.matcher(url);
		if (!urlMatcher.matches()){
			logger.error("Bad log entry {}",line);
			return null;
		}
		String path = urlMatcher.group(1);

		return new LogEntry(LocalDateTime.parse(matcher.group(1), df).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
				matcher.group(2),
				matcher.group(5),
				Float.valueOf(matcher.group(7)),
				Float.valueOf(matcher.group(8)),
				Integer.valueOf(matcher.group(10)),
				Integer.valueOf(matcher.group(11)),
				matcher.group(14),
				path
		);
	}


}
