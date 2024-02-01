package android.view.textclassifier;

import android.app.Person;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Pair;
import android.view.textclassifier.ConversationActions;
import android.view.textclassifier.SelectionSessionLogger;
import android.view.textclassifier.intent.LabeledIntent;
import android.view.textclassifier.intent.TemplateIntentFactory;
import com.android.internal.annotations.VisibleForTesting;
import com.google.android.textclassifier.ActionsSuggestionsModel;
import com.google.android.textclassifier.RemoteActionTemplate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes3.dex */
public final class ActionsSuggestionsHelper {
    private static final int FIRST_NON_LOCAL_USER = 1;
    private static final String TAG = "ActionsSuggestions";
    private static final int USER_LOCAL = 0;

    private ActionsSuggestionsHelper() {
    }

    public static ActionsSuggestionsModel.ConversationMessage[] toNativeMessages(List<ConversationActions.Message> messages, Function<CharSequence, String> languageDetector) {
        long referenceTime;
        String timeZone;
        List<ConversationActions.Message> messagesWithText = (List) messages.stream().filter(new Predicate() { // from class: android.view.textclassifier.-$$Lambda$ActionsSuggestionsHelper$6oTtcn9bDE-u-8FbiyGdntqoQG0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ActionsSuggestionsHelper.lambda$toNativeMessages$0((ConversationActions.Message) obj);
            }
        }).collect(Collectors.toCollection(new Supplier() { // from class: android.view.textclassifier.-$$Lambda$OGSS2qx6njxlnp0dnKb4lA3jnw8
            @Override // java.util.function.Supplier
            public final Object get() {
                return new ArrayList();
            }
        }));
        if (messagesWithText.isEmpty()) {
            return new ActionsSuggestionsModel.ConversationMessage[0];
        }
        Deque<ActionsSuggestionsModel.ConversationMessage> nativeMessages = new ArrayDeque<>();
        PersonEncoder personEncoder = new PersonEncoder();
        int size = messagesWithText.size();
        for (int i = size - 1; i >= 0; i--) {
            ConversationActions.Message message = messagesWithText.get(i);
            if (message.getReferenceTime() == null) {
                referenceTime = 0;
            } else {
                referenceTime = message.getReferenceTime().toInstant().toEpochMilli();
            }
            if (message.getReferenceTime() == null) {
                timeZone = null;
            } else {
                timeZone = message.getReferenceTime().getZone().getId();
            }
            nativeMessages.push(new ActionsSuggestionsModel.ConversationMessage(personEncoder.encode(message.getAuthor()), message.getText().toString(), referenceTime, timeZone, languageDetector.apply(message.getText())));
        }
        return (ActionsSuggestionsModel.ConversationMessage[]) nativeMessages.toArray(new ActionsSuggestionsModel.ConversationMessage[nativeMessages.size()]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$toNativeMessages$0(ConversationActions.Message message) {
        return !TextUtils.isEmpty(message.getText());
    }

    public static String createResultId(Context context, List<ConversationActions.Message> messages, int modelVersion, List<Locale> modelLocales) {
        StringJoiner localesJoiner = new StringJoiner(SmsManager.REGEX_PREFIX_DELIMITER);
        for (Locale locale : modelLocales) {
            localesJoiner.add(locale.toLanguageTag());
        }
        String modelName = String.format(Locale.US, "%s_v%d", localesJoiner.toString(), Integer.valueOf(modelVersion));
        int hash = Objects.hash(messages.stream().mapToInt(new ToIntFunction() { // from class: android.view.textclassifier.-$$Lambda$ActionsSuggestionsHelper$YTQv8oPvlmJL4tITUFD4z4JWKRk
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                int hashMessage;
                hashMessage = ActionsSuggestionsHelper.hashMessage((ConversationActions.Message) obj);
                return hashMessage;
            }
        }), context.getPackageName(), Long.valueOf(System.currentTimeMillis()));
        return SelectionSessionLogger.SignatureParser.createSignature(TextClassifier.DEFAULT_LOG_TAG, modelName, hash);
    }

    public static LabeledIntent.Result createLabeledIntentResult(Context context, TemplateIntentFactory templateIntentFactory, ActionsSuggestionsModel.ActionSuggestion nativeSuggestion) {
        RemoteActionTemplate[] remoteActionTemplates = nativeSuggestion.getRemoteActionTemplates();
        if (remoteActionTemplates == null) {
            Log.w(TAG, "createRemoteAction: Missing template for type " + nativeSuggestion.getActionType());
            return null;
        }
        List<LabeledIntent> labeledIntents = templateIntentFactory.create(remoteActionTemplates);
        if (labeledIntents.isEmpty()) {
            return null;
        }
        LabeledIntent.TitleChooser titleChooser = createTitleChooser(nativeSuggestion.getActionType());
        return labeledIntents.get(0).resolve(context, titleChooser, null);
    }

    public static LabeledIntent.TitleChooser createTitleChooser(String actionType) {
        if (ConversationAction.TYPE_OPEN_URL.equals(actionType)) {
            return new LabeledIntent.TitleChooser() { // from class: android.view.textclassifier.-$$Lambda$ActionsSuggestionsHelper$sY0w9od2zcl4YFel0lG4VB3vf7I
                @Override // android.view.textclassifier.intent.LabeledIntent.TitleChooser
                public final CharSequence chooseTitle(LabeledIntent labeledIntent, ResolveInfo resolveInfo) {
                    return ActionsSuggestionsHelper.lambda$createTitleChooser$1(labeledIntent, resolveInfo);
                }
            };
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ CharSequence lambda$createTitleChooser$1(LabeledIntent labeledIntent, ResolveInfo resolveInfo) {
        if (resolveInfo.handleAllWebDataURI) {
            return labeledIntent.titleWithEntity;
        }
        if ("android".equals(resolveInfo.activityInfo.packageName)) {
            return labeledIntent.titleWithEntity;
        }
        return labeledIntent.titleWithoutEntity;
    }

    public static List<ConversationAction> removeActionsWithDuplicates(List<ConversationAction> conversationActions) {
        Map<Pair<String, String>, Integer> counter = new ArrayMap<>();
        for (ConversationAction conversationAction : conversationActions) {
            Pair<String, String> representation = getRepresentation(conversationAction);
            if (representation != null) {
                Integer existingCount = counter.getOrDefault(representation, 0);
                counter.put(representation, Integer.valueOf(existingCount.intValue() + 1));
            }
        }
        List<ConversationAction> result = new ArrayList<>();
        for (ConversationAction conversationAction2 : conversationActions) {
            Pair<String, String> representation2 = getRepresentation(conversationAction2);
            if (representation2 == null || counter.getOrDefault(representation2, 0).intValue() == 1) {
                result.add(conversationAction2);
            }
        }
        return result;
    }

    private static Pair<String, String> getRepresentation(ConversationAction conversationAction) {
        RemoteAction remoteAction = conversationAction.getAction();
        if (remoteAction == null) {
            return null;
        }
        Intent actionIntent = ExtrasUtils.getActionIntent(conversationAction.getExtras());
        ComponentName componentName = actionIntent.getComponent();
        String packageName = componentName != null ? componentName.getPackageName() : null;
        return new Pair<>(conversationAction.getAction().getTitle().toString(), packageName);
    }

    /* loaded from: classes3.dex */
    private static final class PersonEncoder {
        private final Map<Person, Integer> mMapping;
        private int mNextUserId;

        private PersonEncoder() {
            this.mMapping = new ArrayMap();
            this.mNextUserId = 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int encode(Person person) {
            if (ConversationActions.Message.PERSON_USER_SELF.equals(person)) {
                return 0;
            }
            Integer result = this.mMapping.get(person);
            if (result == null) {
                this.mMapping.put(person, Integer.valueOf(this.mNextUserId));
                result = Integer.valueOf(this.mNextUserId);
                this.mNextUserId++;
            }
            return result.intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashMessage(ConversationActions.Message message) {
        return Objects.hash(message.getAuthor(), message.getText(), message.getReferenceTime());
    }
}
