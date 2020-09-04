package com.satyam.clubgariya.utils;

import java.util.List;

public class AppConstants {


    //Intent Constants
    public static int REQUEST_CODE_SELECT_MEDIA = 11;

    public static boolean IS_CONTACT_SERVICE_RUNNING = false;

    public static String CURRENT_CHAT_ID = "";
    public static String CURRENT_TRANSACTION_ID = "";
    public static String USER_COUNTRY_CODE = "county_code";


    public static String VIEW_STACK = "current_stack";
    public static final String VIEW_STACK_MESSAGE_TRANSACTION = "MESSAGE_TRANSACTION";
    public static final String VIEW_STACK_TRANSACTION_MESSAGE = "TRANSACTION_MESSAGE";
    public static final String VIEW_STACK_MESSAGE = "MESSAGE";
    public static final String VIEW_STACK_TRANSACTION = "TRANSACTION";

    //Notification Channel
    public static final String NOTIFICATION_CHANNEL_MESSAGE = "message";
    public static final String NOTIFICATION_CHANNEL_TRANSACTION = "transaction";


    // FireBase Constants

    public static String APP_SETTINGS_COLLECTION_NODE = "AppSettings";
    public static String APP_SETTINGS_DOCUMENT_NODE = "one_click_preference";
    public static String BLOG_COLLECTION_NODE = "Blogs";
    public static String USER_COLLECTION_NODE = "Users";
    public static String USER_CHAT_REFERENCES_COLLECTION_NODE = "ChatReferences";
    public static String USER_TRANSACTION_REFERENCES_COLLECTION_NODE = "TransactionReferences";
    public static String USER_CHAT_SUB_REFERENCES_COLLECTION_NODE = "ChatSubReferences";
    public static String USER_TRANSACTION_SUB_REFERENCES_COLLECTION_NODE = "TransactionSubReferences";
    public static String CHAT_REFERENCE_COLLECTION_NODE = "ChatReferences";
    public static String CHAT_COLLECTION_NODE = "Chats";
    public static String TRANSACTION_COLLECTION_NODE = "Transactions";
    public static String CHAT_SUB_COLLECTION_NODE = "ChatArray";
    public static String TRANSACTION_SUB_COLLECTION_NODE = "TransactionArray";
    public static String COMMENT_COLLECTION_NODE = "Comments";
    public static String COMMENT_ALL_BLOG_NODE = "BlogComments";
    public static String USERS_CLUB_CONTACTS_COLLECTION = "UserClubContacts";
    public static String USERS_RAW_CONTACTS_COLLECTION = "UserRawContacts";
    public static String USERS_SUB_CONTACTS_COLLECTION = "Contacts";
    public static String CONTACTS_COLLECTION_CLUB_MASTER = "MasterContacts";


    // Blog Status

    public static String BLOG_STATUS_ACTIVE = "Active";
    public static String BLOG_STATUS_IN_ACTIVE = "In-Active";

    // Fragment Title
    public static String BLOG_FRAGMENT_TITLE = "Blogs";
    public static String MESSAGE_FRAGMENT_TITLE = "Messages";
    public static String EVENT_FRAGMENT_TITLE = "Events";
    public static String PAYMENTS_FRAGMENT_TITLE = "Payments";


    //Database Contact
    public static String DATABASE_CONTACT_STATUS_DEFAULT = "Active";
    public static String DATABASE_CONTACT_COUNT = "contact_count";
    public static String DATABASE_CONTACT_SYNC_DATE = "contact_sync_date";


    //Firebase Constant
    public static String FIREBASE_CONTACT_NODE = "mobile";


    // Notification Channels
    public static String CHANNEL_MESSAGE = "message";
    public static String CHANNEL_BLOG = "blog";
    public static String CHANNEL_EVENT = "event";

    // Transaction settlement Status
    public static final String TRANSACTION_INITIATE = "INITIATE";
    public static final String TRANSACTION_DISPUTE = "DISPUTE";
    public static final String TRANSACTION_CONFIRM = "CONFIRM";
    public static final String TRANSACTION_UPDATE_TYPE = "TRANSACTIONAL";


    // Message Delivery Status
    public static final String MESSAGE_SEND = "SEND";
    public static final String MESSAGE_DELIVERED = "DELIVERED";
    public static final String MESSAGE_READ = "READ";


    // User List source
    public static final String USER_LIST_FOR_CHAT = "ChatFragment";
    public static final String USER_LIST_FOR_TRANSACTION = "TransactionFragment";

    public static final String USER_BUSINESS_TYPE_DEFAULT = "NO_BUSINESS";

    public static final String USER_ROLE_MEMBER = "MEMBER";
    public static final String USER_ROLE_ADMIN = "ADMIN";
    public static String USER_PROFILE_STATUS_DEFAULT = "Active";

    public static final String USER_TYPE_INDIVIDUAL = "INDIVIDUAL";
    public static final String USER_TYPE_GROUP = "GROUP";
    public static final String NOTIFICATION_TYPE_TRANSACTION_INITIATED = "TRANSACTION_INITIATED";
    public static final String NOTIFICATION_TYPE_TRANSACTION_CONFIRM = "TRANSACTION_CONFIRM";
    public static final String NOTIFICATION_TYPE_TRANSACTION_DISPUTE = "TRANSACTION_DISPUTE";

    public static final String NOTIFICATION_TYPE_MESSAGE_INITIATED = "MESSAGE_INITIATED";
    public static final String REFERENCE_TYPE_INDIVIDUAL = "INITIATED";
    public static final String REFERENCE_TYPE_GROUP = "GROUP";


    // TAG CHAT REFERENCE
    public static final String CHAT_REFERENCE_TAG_ALLOWED_USERS="allowedUsers";
    public static final String USER_CHAT_GROUP_TAG_="chatGroups";
    public static final String USER_TRANSACTION_GROUP_TAG_="transactionGroups";




}
