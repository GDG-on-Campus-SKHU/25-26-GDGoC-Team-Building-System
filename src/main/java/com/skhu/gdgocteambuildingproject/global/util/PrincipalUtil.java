package com.skhu.gdgocteambuildingproject.global.util;

import java.security.Principal;

public class PrincipalUtil {
    public static long getUserIdFrom(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
