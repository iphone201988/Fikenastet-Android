package com.fisken_astet.fikenastet.data.model

/** user data model **/
data class User(
    var account_integrity: String?,
    val badge_count: Int?,
    val created_at: String?,
    val deleted_at: Any?,
    val device_token: String?,
    val device_type: Int?,
    val email: String?,
    val email_notifications: Int?,
    val email_verified_at: String?,
    val google2fa_secret: Any?,
    val id: Int?,
    val is_2fa_enabled: Int?,
    val is_active: Int?,
    val is_lake_owner: Int?,
    val is_product_owner: Int?,
    val is_verified: Int?,
    val last_seen: String?,
    val latitude: String?,
    val location: String?,
    val longitude: String?,
    val otp_expires_at: Any?,
    val profile_picture: String?,
    var push_notifications: Int?,
    val qr_code: Any?,
    val role: String?,
    val short_bio: String?,
    val skill_level: Int?,
    val stripe_account_id: Any?,
    val stripe_customer_id: Any?,
    val updated_at: String?,
    val user_level: Int?,
    val user_links: List<UserLink?>?,
    val username: String?
)
/** signup model **/
data class SignupModel(
    val access_token: String?,
    val message: String?,
    val success: Int?,
    val user: User?
)

/** otp sent to email **/
data class OtpSentModel(
    val message: String?,
    val otp: Int?,
    val success: Int?
)

/** otp verified model && login model  **/
data class VerifiedUserModel(
    val access_token: String?,
    val message: String?,
    val success: Int?,
    val token_type: String?,
    val user: User?
)

/** profile complete **/
data class ProfileCompleteModel(
    val links: List<Link?>?,
    val message: String?,
    val success: Int?,
    val user: User?
)

data class Link(
    val created_at: String?,
    val id: Int?,
    val title: String?,
    val updated_at: String?,
    val url: String?,
    val user_id: Int?
)

data class UserLink(
    val created_at: String?,
    val id: Int?,
    val title: String?,
    val updated_at: String?,
    val url: String?,
    val user_id: Int?
)

/** forget email model **/
data class ForgetEmailModel(
    val `data`: ForgetEmailModelData?,
    val message: String?,
    val status: Int?
)

data class ForgetEmailModelData(
    val expires_at: String?,
    val otp: Int?
)

/** forget verify model **/
data class ForgetVerifyModel(
    val `data`: ForgetVerifyModelData?,
    val message: String?,
    val status: Int?
)

data class ForgetVerifyModelData(
    val token: String?
)

/** simple model **/
data class SimpleModel(
    val message: String?,
    val status: Int?
)

/** notification toggle model **/
data class NotificationToggleModel(
    val message: String?,
    val push_notifications: Int?,
    val success: Int?
)

/** Two FA ***/
data class TwoFAEnableModel(
    val `data`: TwoFAEnableModelData?,
    val message: String?,
    val status: Int?
)

data class TwoFAEnableModelData(
    val qr_code: String?,
    val secret: String?
)

/** verify via login **/
data class TwoFAVerifyViaLogin(
    val message: String?,
    val status: Int?,
    val user: User?
)

/** account integrity model **/
data class AccountIntegrityModel(
    val badge_count: Int?,
    val `data`: AccountIntegrityModelData?,
    val message: String?,
    val status: Int?
)

data class AccountIntegrityModelData(
    val account_integrity: String?,
    val user_id: Int?
)

/** export data model **/
data class ExportDataModel(
    val download_url: String?,
    val message: String?,
    val status: Int?
)

/** get profile data **/
data class GetProfileModel(
    val badge_count: Int?,
    val `data`: GetProfileModelData?,
    val status: Int?,
    val message: String?
)

data class GetProfileModelData(
    val account_integrity: String?,
    val average_rating: Double?,
    val email: String?,
    val followers_count: Int?,
    val following_count: Int?,
    val links: List<ProfileLink?>?,
    val location: String?,
    val posts: List<Post?>?,
    val posts_count: Int?,
    val profile_picture: String?,
    val short_bio: String?,
    val user_id: Int?,
    val user_level: Int?,
    val username: String?,
    val you_blocked: Boolean?,
    val you_followed: Boolean?,
    val you_restricted: Boolean?
)

data class ProfileLink(
    val title: String?,
    val url: String?
)

data class Post(
    val comments_count: Int?,
    val created_at: String?,
    val fish_caught: String?,
    val hashtags: List<String?>?,
    val images: List<Image?>?,
    val likes_count: Int?,
    val location: String?,
    val post_id: Int?,
    val tag_peoples: List<TagPeople?>?,
    val you_liked: Boolean?
)

data class Image(
    val id: Int?,
    val image_ratio: String?,
    val image_url: String?,
    val type: String?
)

data class TagPeople(
    val id: Int?,
    val profile_picture: String?,
    val username: String?
)

/** Follower Model **/
data class FollowersModel(
    val badge_count: Int?,
    val `data`: List<FollowersModelData?>?,
    val status: Int?
)

data class FollowersModelData(
    val id: Int?,
    val profile_picture: String?,
    val username: String?,
    var you_followed: Boolean?,
    var myId:Int?
)

data class FollowUnfollowToggleModel(
    val action: String?,
    val badge_count: Int?,
    val following_count: Int?,
    val message: String?,
    val status: Int?
)

/** report **/
data class ReportModel(
    val badge_count: Int?,
    val `data`: List<ReportModelData?>?,
    val message: String?,
    val status: Int?
)

data class ReportModelData(
    val created_at: String?,
    val description: Any?,
    val id: Int?,
    val report_type_id: Int?,
    val reported_by: Int?,
    val reported_user_id: String?,
    val updated_at: String?
)

/** blocked user list **/
data class BlockedUserList(
    val badge_count: Int?,
    val `data`: List<BlockedUserListData?>?,
    val message: String?,
    val status: Int?
)

data class BlockedUserListData(
    val id: Int?,
    val profile_picture: String?,
    val username: String?,
    val you_blocked: Boolean?
)



