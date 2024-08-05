package com.neonai.axocomplaints.smpleclasses;

public class Const {

    /*public static final String MAIN = "https://tanajisawant.org/public/";
*/

    public static final String MAIN = "https://tsfc.axolotlsapps.in/public/";
    public static final String LOGIN = MAIN+"api/createNewUser";
    public static final String VERIFY_OTP = MAIN+"api/verifyAccount";

    public static final String PROFILE = MAIN+"api/retriveProfileAccount";
    public static final String PROFILE_UPDATE = MAIN+"api/updateProfileAccount";

    public static final String CREATE_TICKET= MAIN+"api/raiseANewTicket";
    public static final String TOTAL_TICKET= MAIN+"api/listOfRaisedTicket";
    public static final String ACTIVE_TICKET= MAIN+"api/listOfOpenTicket";
//    public static final String CLOSED_TICKET= MAIN+"api/listOfClosedTicket";
    public static final String CLOSED_TICKET= MAIN+"api/listOfResolvedTicket";
    public static final String FEEDBACK= MAIN+"api/createFeedback";
    public static final String FEEDBACK_GET= MAIN+"api/FeedbackDetails";
    public static final String IMAGE_GET= MAIN+"api/profile_image";

    public static final String PROFILE_GET= MAIN+"api/retriveProfileAccount";
    public static final String DELETE_TICKET= MAIN+"api/deleteTicket";

}
