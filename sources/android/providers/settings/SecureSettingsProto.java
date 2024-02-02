package android.providers.settings;
/* loaded from: classes2.dex */
public final class SecureSettingsProto {
    private protected static final long ACCESSIBILITY = 1146756268034L;
    private protected static final long ALLOWED_GEOLOCATION_ORIGINS = 1146756268035L;
    private protected static final long ALWAYS_ON_VPN = 1146756268036L;
    private protected static final long ANDROID_ID = 1146756268037L;
    private protected static final long ANR_SHOW_BACKGROUND = 1146756268038L;
    private protected static final long ASSIST = 1146756268039L;
    private protected static final long AUTOFILL = 1146756268040L;
    private protected static final long AUTOMATIC_STORAGE_MANAGER = 1146756268041L;
    private protected static final long BACKUP = 1146756268042L;
    private protected static final long BLUETOOTH_ON_WHILE_DRIVING = 1146756268043L;
    private protected static final long CAMERA = 1146756268044L;
    private protected static final long CARRIER_APPS_HANDLED = 1146756268045L;
    private protected static final long CMAS_ADDITIONAL_BROADCAST_PKG = 1146756268046L;
    private protected static final long COMPLETED_CATEGORIES = 2246267895823L;
    private protected static final long CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS = 1146756268048L;
    private protected static final long DEVICE_PAIRED = 1146756268049L;
    private protected static final long DIALER_DEFAULT_APPLICATION = 1146756268050L;
    private protected static final long DISPLAY_DENSITY_FORCED = 1146756268051L;
    private protected static final long DOUBLE_TAP_TO_WAKE = 1146756268052L;
    private protected static final long DOZE = 1146756268053L;
    private protected static final long EMERGENCY_ASSISTANCE_APPLICATION = 1146756268054L;
    private protected static final long ENHANCED_VOICE_PRIVACY_ENABLED = 1146756268055L;
    private protected static final long HISTORICAL_OPERATIONS = 2246267895809L;
    private protected static final long IMMERSIVE_MODE_CONFIRMATIONS = 1146756268056L;
    private protected static final long INCALL = 1146756268057L;
    private protected static final long INPUT_METHODS = 1146756268058L;
    private protected static final long INSTALL_NON_MARKET_APPS = 1146756268059L;
    private protected static final long INSTANT_APPS_ENABLED = 1146756268060L;
    private protected static final long KEYGUARD_SLICE_URI = 1146756268061L;
    private protected static final long LAST_SETUP_SHOWN = 1146756268062L;
    private protected static final long LAUNCHER = 1146756268102L;
    private protected static final long LOCATION = 1146756268063L;
    private protected static final long LOCKDOWN_IN_POWER_MENU = 1146756268066L;
    private protected static final long LOCK_SCREEN = 1146756268064L;
    private protected static final long LOCK_TO_APP_EXIT_LOCKED = 1146756268065L;
    private protected static final long LONG_PRESS_TIMEOUT = 1146756268067L;
    private protected static final long MANAGED_PROFILE = 1146756268068L;
    private protected static final long MOUNT = 1146756268069L;
    private protected static final long MULTI_PRESS_TIMEOUT = 1146756268070L;
    private protected static final long NFC_PAYMENT = 1146756268071L;
    private protected static final long NIGHT_DISPLAY = 1146756268072L;
    private protected static final long NOTIFICATION = 1146756268073L;
    private protected static final long PACKAGE_VERIFIER = 1146756268074L;
    private protected static final long PARENTAL_CONTROL = 1146756268075L;
    private protected static final long PRINT_SERVICE = 1146756268076L;
    private protected static final long QS = 1146756268077L;
    private protected static final long ROTATION = 1146756268078L;
    private protected static final long RTT_CALLING_MODE = 1146756268101L;
    private protected static final long SCREENSAVER = 1146756268079L;
    private protected static final long SEARCH = 1146756268080L;
    private protected static final long SETTINGS_CLASSNAME = 1146756268082L;
    private protected static final long SHOW_FIRST_CRASH_DIALOG_DEV_OPTION = 1146756268083L;
    private protected static final long SKIP_FIRST_USE_HINTS = 1146756268084L;
    private protected static final long SLEEP_TIMEOUT = 1146756268085L;
    private protected static final long SMS_DEFAULT_APPLICATION = 1146756268086L;
    private protected static final long SPELL_CHECKER = 1146756268081L;
    private protected static final long SYNC_PARENT_SOUNDS = 1146756268087L;
    private protected static final long SYSTEM_NAVIGATION_KEYS_ENABLED = 1146756268088L;
    private protected static final long TRUST_AGENTS_INITIALIZED = 1146756268089L;
    private protected static final long TTS = 1146756268090L;
    private protected static final long TTY = 1146756268091L;
    private protected static final long TV = 1146756268092L;
    private protected static final long UI_NIGHT_MODE = 1146756268093L;
    private protected static final long UNKNOWN_SOURCES_DEFAULT_REVERSED = 1146756268094L;
    private protected static final long USB_AUDIO_AUTOMATIC_ROUTING_DISABLED = 1146756268095L;
    private protected static final long USER_SETUP_COMPLETE = 1146756268096L;
    private protected static final long VOICE = 1146756268097L;
    private protected static final long VOLUME = 1146756268098L;
    private protected static final long VR = 1146756268099L;
    private protected static final long WAKE_GESTURE_ENABLED = 1146756268100L;

    private protected synchronized SecureSettingsProto() {
    }

    /* loaded from: classes2.dex */
    public final class Accessibility {
        private protected static final long AUTOCLICK_DELAY = 1146756268036L;
        private protected static final long AUTOCLICK_ENABLED = 1146756268035L;
        private protected static final long BUTTON_TARGET_COMPONENT = 1146756268037L;
        private protected static final long CAPTIONING_BACKGROUND_COLOR = 1146756268041L;
        private protected static final long CAPTIONING_EDGE_COLOR = 1146756268044L;
        private protected static final long CAPTIONING_EDGE_TYPE = 1146756268043L;
        private protected static final long CAPTIONING_ENABLED = 1146756268038L;
        private protected static final long CAPTIONING_FONT_SCALE = 1146756268047L;
        private protected static final long CAPTIONING_FOREGROUND_COLOR = 1146756268042L;
        private protected static final long CAPTIONING_LOCALE = 1146756268039L;
        private protected static final long CAPTIONING_PRESET = 1146756268040L;
        private protected static final long CAPTIONING_TYPEFACE = 1146756268046L;
        private protected static final long CAPTIONING_WINDOW_COLOR = 1146756268045L;
        private protected static final long DISPLAY_DALTONIZER = 1146756268049L;
        private protected static final long DISPLAY_DALTONIZER_ENABLED = 1146756268048L;
        private protected static final long DISPLAY_INVERSION_ENABLED = 1146756268050L;
        private protected static final long DISPLAY_MAGNIFICATION_ENABLED = 1146756268051L;
        private protected static final long DISPLAY_MAGNIFICATION_NAVBAR_ENABLED = 1146756268052L;
        private protected static final long DISPLAY_MAGNIFICATION_SCALE = 1146756268053L;
        private protected static final long ENABLED = 1146756268033L;
        private protected static final long ENABLED_ACCESSIBILITY_SERVICES = 1146756268034L;
        private protected static final long HIGH_TEXT_CONTRAST_ENABLED = 1146756268054L;
        private protected static final long LARGE_POINTER_ICON = 1146756268055L;
        private protected static final long SHORTCUT_DIALOG_SHOWN = 1146756268058L;
        private protected static final long SHORTCUT_ENABLED = 1146756268056L;
        private protected static final long SHORTCUT_ON_LOCK_SCREEN = 1146756268057L;
        private protected static final long SHORTCUT_TARGET_SERVICE = 1146756268059L;
        private protected static final long SOFT_KEYBOARD_MODE = 1146756268060L;
        private protected static final long SPEAK_PASSWORD = 1146756268061L;
        private protected static final long TOUCH_EXPLORATION_ENABLED = 1146756268062L;
        private protected static final long TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES = 1146756268063L;

        public Accessibility() {
        }
    }

    /* loaded from: classes2.dex */
    public final class AlwaysOnVpn {
        private protected static final long APP = 1146756268033L;
        private protected static final long LOCKDOWN = 1146756268034L;

        public AlwaysOnVpn() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Assist {
        private protected static final long ASSISTANT = 1146756268033L;
        private protected static final long DISCLOSURE_ENABLED = 1146756268036L;
        private protected static final long GESTURE_ENABLED = 1146756268037L;
        private protected static final long GESTURE_SENSITIVITY = 1146756268038L;
        private protected static final long GESTURE_SETUP_COMPLETE = 1146756268041L;
        private protected static final long GESTURE_SILENCE_ALERTS_ENABLED = 1146756268039L;
        private protected static final long GESTURE_WAKE_ENABLED = 1146756268040L;
        private protected static final long SCREENSHOT_ENABLED = 1146756268035L;
        private protected static final long STRUCTURE_ENABLED = 1146756268034L;

        public Assist() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Autofill {
        private protected static final long FEATURE_FIELD_CLASSIFICATION = 1146756268034L;
        private protected static final long SERVICE = 1146756268033L;
        private protected static final long SERVICE_SEARCH_URI = 1146756268040L;
        private protected static final long USER_DATA_MAX_CATEGORY_COUNT = 1146756268037L;
        private protected static final long USER_DATA_MAX_FIELD_CLASSIFICATION_IDS_SIZE = 1146756268036L;
        private protected static final long USER_DATA_MAX_USER_DATA_SIZE = 1146756268035L;
        private protected static final long USER_DATA_MAX_VALUE_LENGTH = 1146756268038L;
        private protected static final long USER_DATA_MIN_VALUE_LENGTH = 1146756268039L;

        public Autofill() {
        }
    }

    /* loaded from: classes2.dex */
    public final class AutomaticStorageManager {
        private protected static final long BYTES_CLEARED = 1146756268035L;
        private protected static final long DAYS_TO_RETAIN = 1146756268034L;
        private protected static final long ENABLED = 1146756268033L;
        private protected static final long LAST_RUN = 1146756268036L;
        private protected static final long TURNED_OFF_BY_POLICY = 1146756268037L;

        public AutomaticStorageManager() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Backup {
        private protected static final long AUTO_RESTORE = 1146756268034L;
        private protected static final long ENABLED = 1146756268033L;
        private protected static final long LOCAL_TRANSPORT_PARAMETERS = 1146756268038L;
        private protected static final long MANAGER_CONSTANTS = 1146756268037L;
        private protected static final long PACKAGES_TO_CLEAR_DATA_BEFORE_FULL_RESTORE = 1146756268039L;
        private protected static final long PROVISIONED = 1146756268035L;
        private protected static final long TRANSPORT = 1146756268036L;

        public Backup() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Camera {
        private protected static final long DOUBLE_TAP_POWER_GESTURE_DISABLED = 1146756268034L;
        private protected static final long DOUBLE_TWIST_TO_FLIP_ENABLED = 1146756268035L;
        private protected static final long GESTURE_DISABLED = 1146756268033L;
        private protected static final long LIFT_TRIGGER_ENABLED = 1146756268036L;

        public Camera() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Doze {
        private protected static final long ALWAYS_ON = 1146756268034L;
        private protected static final long ENABLED = 1146756268033L;
        private protected static final long PULSE_ON_DOUBLE_TAP = 1146756268037L;
        private protected static final long PULSE_ON_LONG_PRESS = 1146756268036L;
        private protected static final long PULSE_ON_PICK_UP = 1146756268035L;

        public Doze() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Incall {
        private protected static final long BACK_BUTTON_BEHAVIOR = 1146756268034L;
        private protected static final long POWER_BUTTON_BEHAVIOR = 1146756268033L;

        public Incall() {
        }
    }

    /* loaded from: classes2.dex */
    public final class InputMethods {
        private protected static final long DEFAULT_INPUT_METHOD = 1146756268033L;
        private protected static final long DISABLED_SYSTEM_INPUT_METHODS = 1146756268034L;
        private protected static final long ENABLED_INPUT_METHODS = 1146756268035L;
        private protected static final long METHOD_SELECTOR_VISIBILITY = 1146756268036L;
        private protected static final long SELECTED_INPUT_METHOD_SUBTYPE = 1146756268038L;
        private protected static final long SHOW_IME_WITH_HARD_KEYBOARD = 1146756268039L;
        private protected static final long SUBTYPE_HISTORY = 1146756268037L;

        public InputMethods() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Launcher {
        private protected static final long SWIPE_UP_TO_SWITCH_APPS_ENABLED = 1146756268033L;

        public Launcher() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Location {
        private protected static final long CHANGER = 1146756268034L;
        private protected static final long MODE = 1146756268033L;

        public Location() {
        }
    }

    /* loaded from: classes2.dex */
    public final class LockScreen {
        private protected static final long ALLOW_PRIVATE_NOTIFICATIONS = 1146756268034L;
        private protected static final long ALLOW_REMOTE_INPUT = 1146756268035L;
        private protected static final long LOCK_AFTER_TIMEOUT = 1146756268033L;
        private protected static final long SHOW_NOTIFICATIONS = 1146756268036L;

        public LockScreen() {
        }
    }

    /* loaded from: classes2.dex */
    public final class ManagedProfile {
        private protected static final long CONTACT_REMOTE_SEARCH = 1146756268033L;

        public ManagedProfile() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Mount {
        private protected static final long PLAY_NOTIFICATION_SND = 1146756268033L;
        private protected static final long UMS_AUTOSTART = 1146756268034L;
        private protected static final long UMS_NOTIFY_ENABLED = 1146756268036L;
        private protected static final long UMS_PROMPT = 1146756268035L;

        public Mount() {
        }
    }

    /* loaded from: classes2.dex */
    public final class NfcPayment {
        private protected static final long DEFAULT_COMPONENT = 1146756268033L;
        private protected static final long FOREGROUND = 1146756268034L;
        private protected static final long PAYMENT_SERVICE_SEARCH_URI = 1146756268035L;

        public NfcPayment() {
        }
    }

    /* loaded from: classes2.dex */
    public final class NightDisplay {
        private protected static final long ACTIVATED = 1146756268033L;
        private protected static final long AUTO_MODE = 1146756268034L;
        private protected static final long COLOR_TEMPERATURE = 1146756268035L;
        private protected static final long CUSTOM_END_TIME = 1146756268037L;
        private protected static final long CUSTOM_START_TIME = 1146756268036L;
        private protected static final long LAST_ACTIVATED_TIME = 1146756268038L;

        public NightDisplay() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Notification {
        private protected static final long BADGING = 1146756268036L;
        private protected static final long ENABLED_ASSISTANT = 1146756268033L;
        private protected static final long ENABLED_LISTENERS = 1146756268034L;
        private protected static final long ENABLED_POLICY_ACCESS_PACKAGES = 1146756268035L;
        private protected static final long SHOW_NOTE_ABOUT_NOTIFICATION_HIDING = 1146756268037L;

        public Notification() {
        }
    }

    /* loaded from: classes2.dex */
    public final class PackageVerifier {
        private protected static final long STATE = 1146756268034L;
        private protected static final long USER_CONSENT = 1146756268033L;

        public PackageVerifier() {
        }
    }

    /* loaded from: classes2.dex */
    public final class ParentalControl {
        private protected static final long ENABLED = 1146756268033L;
        private protected static final long LAST_UPDATE = 1146756268034L;
        private protected static final long REDIRECT_URL = 1146756268035L;

        public ParentalControl() {
        }
    }

    /* loaded from: classes2.dex */
    public final class PrintService {
        private protected static final long DISABLED_PRINT_SERVICES = 1146756268034L;
        private protected static final long ENABLED_PRINT_SERVICES = 1146756268035L;
        private protected static final long SEARCH_URI = 1146756268033L;

        public PrintService() {
        }
    }

    /* loaded from: classes2.dex */
    public final class QuickSettings {
        private protected static final long AUTO_ADDED_TILES = 1146756268034L;
        private protected static final long TILES = 1146756268033L;

        public QuickSettings() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Rotation {
        private protected static final long NUM_ROTATION_SUGGESTIONS_ACCEPTED = 1146756268034L;
        private protected static final long SHOW_ROTATION_SUGGESTIONS = 1146756268033L;

        public Rotation() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Screensaver {
        private protected static final long ACTIVATE_ON_DOCK = 1146756268035L;
        private protected static final long ACTIVATE_ON_SLEEP = 1146756268036L;
        private protected static final long COMPONENTS = 1146756268034L;
        private protected static final long DEFAULT_COMPONENT = 1146756268037L;
        private protected static final long ENABLED = 1146756268033L;

        public Screensaver() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Search {
        private protected static final long GLOBAL_SEARCH_ACTIVITY = 1146756268033L;
        private protected static final long MAX_RESULTS_PER_SOURCE = 1146756268036L;
        private protected static final long MAX_RESULTS_TO_DISPLAY = 1146756268035L;
        private protected static final long MAX_SHORTCUTS_RETURNED = 1146756268045L;
        private protected static final long MAX_SOURCE_EVENT_AGE_MILLIS = 1146756268042L;
        private protected static final long MAX_STAT_AGE_MILLIS = 1146756268041L;
        private protected static final long MIN_CLICKS_FOR_SOURCE_RANKING = 1146756268044L;
        private protected static final long MIN_IMPRESSIONS_FOR_SOURCE_RANKING = 1146756268043L;
        private protected static final long NUM_PROMOTED_SOURCES = 1146756268034L;
        private protected static final long PER_SOURCE_CONCURRENT_QUERY_LIMIT = 1146756268051L;
        private protected static final long PREFILL_MILLIS = 1146756268040L;
        private protected static final long PROMOTED_SOURCE_DEADLINE_MILLIS = 1146756268038L;
        private protected static final long QUERY_THREAD_CORE_POOL_SIZE = 1146756268046L;
        private protected static final long QUERY_THREAD_MAX_POOL_SIZE = 1146756268047L;
        private protected static final long SHORTCUT_REFRESH_CORE_POOL_SIZE = 1146756268048L;
        private protected static final long SHORTCUT_REFRESH_MAX_POOL_SIZE = 1146756268049L;
        private protected static final long SOURCE_TIMEOUT_MILLIS = 1146756268039L;
        private protected static final long THREAD_KEEPALIVE_SECONDS = 1146756268050L;
        private protected static final long WEB_RESULTS_OVERRIDE_LIMIT = 1146756268037L;

        public Search() {
        }
    }

    /* loaded from: classes2.dex */
    public final class SpellChecker {
        private protected static final long ENABLED = 1146756268033L;
        private protected static final long SELECTED = 1146756268034L;
        private protected static final long SELECTED_SUBTYPE = 1146756268035L;

        public SpellChecker() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Tts {
        private protected static final long DEFAULT_LOCALE = 1146756268036L;
        private protected static final long DEFAULT_PITCH = 1146756268034L;
        private protected static final long DEFAULT_RATE = 1146756268033L;
        private protected static final long DEFAULT_SYNTH = 1146756268035L;
        private protected static final long ENABLED_PLUGINS = 1146756268037L;

        public Tts() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Tty {
        private protected static final long PREFERRED_TTY_MODE = 1146756268034L;
        private protected static final long TTY_MODE_ENABLED = 1146756268033L;

        public Tty() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Tv {
        private protected static final long INPUT_CUSTOM_LABELS = 1146756268035L;
        private protected static final long INPUT_HIDDEN_INPUTS = 1146756268034L;
        private protected static final long USER_SETUP_COMPLETE = 1146756268033L;

        public Tv() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Voice {
        private protected static final long INTERACTION_SERVICE = 1146756268033L;
        private protected static final long RECOGNITION_SERVICE = 1146756268034L;

        public Voice() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Volume {
        private protected static final long HUSH_GESTURE = 1146756268033L;
        private protected static final long UNSAFE_VOLUME_MUSIC_ACTIVE_MS = 1146756268034L;

        public Volume() {
        }
    }

    /* loaded from: classes2.dex */
    public final class Vr {
        private protected static final long DISPLAY_MODE = 1146756268033L;
        private protected static final long ENABLED_LISTENERS = 1146756268034L;

        public Vr() {
        }
    }
}
