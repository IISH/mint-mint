package gr.ntua.ivml.mint.with;


import net.sf.json.JSONObject;

public class MediaObject {

	String kind;
	String url;
	String type;
	String originalRights;
	String withRights;
	String parentId;

	MediaObject thumbnail;

	public MediaObject() {
		thumbnail = null;

	}

	public static enum WithMediaRights {
		Public("CC0 Public domain"), Restricted("Restricted"), Permission(
				"Permission"), Modify("Allow re-use and modifications"), Commercial(
				"Allow re-use for commercial"),
		/*
		 * Creative_Commercial_Modify(
		 * "use for commercial purposes modify, adapt, or build upon"),
		 * Creative_Not_Commercial("NOT Comercial"),
		 * Creative_Not_Modify("NOT Modify"), Creative_Not_Commercial_Modify(
		 * "not modify, adapt, or build upon, not for commercial purposes"),
		 * Creative_SA("share alike"), Creative_BY("use by attribution"),
		 * Creative("Allow re-use"),
		 */
		Creative_Not_Commercial_Share_Alike(
				"not commercial use the same license"),
		// CC-BY-NC-SA

		// Creative_Not_Commercial_Modify(
		// "use for commercial purposes modify, adapt, or build upon"),

		Creative_Not_Commercial("NOT Comercial"),
		// CC-BY-NC
		Creative_Not_Modify("NOT Modify"),
		// CC-BY-ND
		Creative_Not_Commercial_Modify(
				"not modify, adapt, or build upon, not for commercial purposes"),
		// CC-BY-NC-ND
		Creative_SA("share alike"),
		// CC-BY-SA
		Creative_BY("use by attribution"),
		// CC-BY
		Creative("Allow re-use"),
		// CC0?
		RR("Rights Reserved"), RRPA("Rights Reserved - Paid Access"), RRRA(
				"Rights Reserved - Restricted Access"), RRFA(
				"Rights Reserved - Free Access"), UNKNOWN("Unknown");

		private final String text;

		private WithMediaRights(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

	}

	public WithMediaRights mapRights(String original) {
		String result;
		WithMediaRights wRights = null;

		if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(original,
				"creative")) {

			if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "by-nc-nd")) {
				wRights = WithMediaRights.Creative_Not_Commercial_Modify;
			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "by-nc-sa")) {
				wRights = WithMediaRights.Creative_Not_Commercial_Share_Alike;
			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "by-nc")) {
				wRights = WithMediaRights.Creative_Not_Commercial;

			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "by-nd")) {
				wRights = WithMediaRights.Creative_Not_Modify;

			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "by-sa")) {
				wRights = WithMediaRights.Creative_SA;

			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "by")) {
				wRights = WithMediaRights.Creative_BY;

			} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "cc0")
					|| org.apache.commons.lang3.StringUtils.containsIgnoreCase(
							original, "cc0")) {
				wRights = WithMediaRights.Creative;

			}

		}

		else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
				original, "rights")) {
			if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "rr-f")) {
				wRights = WithMediaRights.RRFA;

			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "rr-r")) {
				wRights = WithMediaRights.RRRA;

			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "rr-p")) {
				wRights = WithMediaRights.RRPA;

			}

			else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					original, "rr")) {
				wRights = WithMediaRights.RR;
			}

		}

		else {
			wRights = WithMediaRights.UNKNOWN;
		}
		return  wRights;
	}

	public net.sf.json.JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("url", url);
		result.put("type", type);
		result.put("originalRights", originalRights);
		result.put("withRights", mapRights(originalRights));
	//	result.put("withRights", mapRights(originalRights));

		result.put("parentId", parentId);

	//	if (this.thumbnail != null) {
	//		result.put("thumbnail", thumbnail.toJson());

	//	}
		return result;

	}

}
