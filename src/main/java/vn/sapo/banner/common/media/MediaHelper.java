package vn.sapo.banner.common.media;

import org.apache.commons.lang3.StringUtils;

public class MediaHelper {

    public static String generateFolderLinkByTenantId(int tenantId, String profile) {
        tenantId = tenantId + 100000000;
        String firstParam = String.format("%03d", tenantId % 1000);
        int result = tenantId / 1000;
        String secondParam = String.format("%03d", result % 1000);
        int result2 = result / 1000;
        String thirdParam = "";
        if (result2 < 1000) {
            thirdParam = String.format("%03d", result2);
        } else {
            thirdParam = String.valueOf(result2);
        }

        StringBuilder path = new StringBuilder();
        if (!StringUtils.contains(profile, "live") && !StringUtils.contains(profile, "staging")){
            path.append("dev");
            path.append("/");
        }
        path.append(thirdParam);
        path.append("/");
        path.append(secondParam);
        path.append("/");
        path.append(firstParam);
        path.append("/");

        return path.toString();
    }

}
