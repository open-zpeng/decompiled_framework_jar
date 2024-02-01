package android.gamedriver;

import com.android.framework.protobuf.AbstractMessageLite;
import com.android.framework.protobuf.ByteString;
import com.android.framework.protobuf.CodedInputStream;
import com.android.framework.protobuf.CodedOutputStream;
import com.android.framework.protobuf.ExtensionRegistryLite;
import com.android.framework.protobuf.GeneratedMessageLite;
import com.android.framework.protobuf.Internal;
import com.android.framework.protobuf.InvalidProtocolBufferException;
import com.android.framework.protobuf.MessageLiteOrBuilder;
import com.android.framework.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class GameDriverProto {

    /* loaded from: classes.dex */
    public interface BlacklistOrBuilder extends MessageLiteOrBuilder {
        String getPackageNames(int i);

        ByteString getPackageNamesBytes(int i);

        int getPackageNamesCount();

        List<String> getPackageNamesList();

        long getVersionCode();

        boolean hasVersionCode();
    }

    /* loaded from: classes.dex */
    public interface BlacklistsOrBuilder extends MessageLiteOrBuilder {
        Blacklist getBlacklists(int i);

        int getBlacklistsCount();

        List<Blacklist> getBlacklistsList();
    }

    private GameDriverProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    /* loaded from: classes.dex */
    public static final class Blacklist extends GeneratedMessageLite<Blacklist, Builder> implements BlacklistOrBuilder {
        private static final Blacklist DEFAULT_INSTANCE = new Blacklist();
        public static final int PACKAGE_NAMES_FIELD_NUMBER = 2;
        private static volatile Parser<Blacklist> PARSER = null;
        public static final int VERSION_CODE_FIELD_NUMBER = 1;
        private int bitField0_;
        private long versionCode_ = 0;
        private Internal.ProtobufList<String> packageNames_ = GeneratedMessageLite.emptyProtobufList();

        private Blacklist() {
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
        public boolean hasVersionCode() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
        public long getVersionCode() {
            return this.versionCode_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setVersionCode(long value) {
            this.bitField0_ |= 1;
            this.versionCode_ = value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearVersionCode() {
            this.bitField0_ &= -2;
            this.versionCode_ = 0L;
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
        public List<String> getPackageNamesList() {
            return this.packageNames_;
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
        public int getPackageNamesCount() {
            return this.packageNames_.size();
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
        public String getPackageNames(int index) {
            return this.packageNames_.get(index);
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
        public ByteString getPackageNamesBytes(int index) {
            return ByteString.copyFromUtf8(this.packageNames_.get(index));
        }

        private void ensurePackageNamesIsMutable() {
            if (!this.packageNames_.isModifiable()) {
                this.packageNames_ = GeneratedMessageLite.mutableCopy(this.packageNames_);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPackageNames(int index, String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensurePackageNamesIsMutable();
            this.packageNames_.set(index, value);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPackageNames(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensurePackageNamesIsMutable();
            this.packageNames_.add(value);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllPackageNames(Iterable<String> values) {
            ensurePackageNamesIsMutable();
            AbstractMessageLite.addAll(values, this.packageNames_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearPackageNames() {
            this.packageNames_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPackageNamesBytes(ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensurePackageNamesIsMutable();
            this.packageNames_.add(value.toStringUtf8());
        }

        @Override // com.android.framework.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt64(1, this.versionCode_);
            }
            for (int i = 0; i < this.packageNames_.size(); i++) {
                output.writeString(2, this.packageNames_.get(i));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.android.framework.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeInt64Size(1, this.versionCode_) : 0;
            int dataSize = 0;
            for (int i = 0; i < this.packageNames_.size(); i++) {
                dataSize += CodedOutputStream.computeStringSizeNoTag(this.packageNames_.get(i));
            }
            int size3 = size2 + dataSize + (getPackageNamesList().size() * 1) + this.unknownFields.getSerializedSize();
            this.memoizedSerializedSize = size3;
            return size3;
        }

        public static Blacklist parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklist parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklist parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklist parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklist parseFrom(InputStream input) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklist parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklist parseDelimitedFrom(InputStream input) throws IOException {
            return (Blacklist) parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklist parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklist) parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklist parseFrom(CodedInputStream input) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklist parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Blacklist prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom((Builder) prototype);
        }

        /* loaded from: classes.dex */
        public static final class Builder extends GeneratedMessageLite.Builder<Blacklist, Builder> implements BlacklistOrBuilder {
            private Builder() {
                super(Blacklist.DEFAULT_INSTANCE);
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
            public boolean hasVersionCode() {
                return ((Blacklist) this.instance).hasVersionCode();
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
            public long getVersionCode() {
                return ((Blacklist) this.instance).getVersionCode();
            }

            public Builder setVersionCode(long value) {
                copyOnWrite();
                ((Blacklist) this.instance).setVersionCode(value);
                return this;
            }

            public Builder clearVersionCode() {
                copyOnWrite();
                ((Blacklist) this.instance).clearVersionCode();
                return this;
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
            public List<String> getPackageNamesList() {
                return Collections.unmodifiableList(((Blacklist) this.instance).getPackageNamesList());
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
            public int getPackageNamesCount() {
                return ((Blacklist) this.instance).getPackageNamesCount();
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
            public String getPackageNames(int index) {
                return ((Blacklist) this.instance).getPackageNames(index);
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistOrBuilder
            public ByteString getPackageNamesBytes(int index) {
                return ((Blacklist) this.instance).getPackageNamesBytes(index);
            }

            public Builder setPackageNames(int index, String value) {
                copyOnWrite();
                ((Blacklist) this.instance).setPackageNames(index, value);
                return this;
            }

            public Builder addPackageNames(String value) {
                copyOnWrite();
                ((Blacklist) this.instance).addPackageNames(value);
                return this;
            }

            public Builder addAllPackageNames(Iterable<String> values) {
                copyOnWrite();
                ((Blacklist) this.instance).addAllPackageNames(values);
                return this;
            }

            public Builder clearPackageNames() {
                copyOnWrite();
                ((Blacklist) this.instance).clearPackageNames();
                return this;
            }

            public Builder addPackageNamesBytes(ByteString value) {
                copyOnWrite();
                ((Blacklist) this.instance).addPackageNamesBytes(value);
                return this;
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.android.framework.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch (method) {
                case NEW_MUTABLE_INSTANCE:
                    return new Blacklist();
                case IS_INITIALIZED:
                    return DEFAULT_INSTANCE;
                case MAKE_IMMUTABLE:
                    this.packageNames_.makeImmutable();
                    return null;
                case NEW_BUILDER:
                    return new Builder();
                case VISIT:
                    GeneratedMessageLite.Visitor visitor = (GeneratedMessageLite.Visitor) arg0;
                    Blacklist other = (Blacklist) arg1;
                    this.versionCode_ = visitor.visitLong(hasVersionCode(), this.versionCode_, other.hasVersionCode(), other.versionCode_);
                    this.packageNames_ = visitor.visitList(this.packageNames_, other.packageNames_);
                    if (visitor == GeneratedMessageLite.MergeFromVisitor.INSTANCE) {
                        this.bitField0_ |= other.bitField0_;
                    }
                    return this;
                case MERGE_FROM_STREAM:
                    CodedInputStream input = (CodedInputStream) arg0;
                    ExtensionRegistryLite extensionRegistryLite = (ExtensionRegistryLite) arg1;
                    boolean done = false;
                    while (!done) {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 8) {
                                this.bitField0_ |= 1;
                                this.versionCode_ = input.readInt64();
                            } else if (tag != 18) {
                                if (!parseUnknownField(tag, input)) {
                                    done = true;
                                }
                            } else {
                                String s = input.readString();
                                if (!this.packageNames_.isModifiable()) {
                                    this.packageNames_ = GeneratedMessageLite.mutableCopy(this.packageNames_);
                                }
                                this.packageNames_.add(s);
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e.setUnfinishedMessage(this));
                        } catch (IOException e2) {
                            throw new RuntimeException(new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this));
                        }
                    }
                    break;
                case GET_DEFAULT_INSTANCE:
                    break;
                case GET_PARSER:
                    if (PARSER == null) {
                        synchronized (Blacklist.class) {
                            if (PARSER == null) {
                                PARSER = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                            }
                        }
                    }
                    return PARSER;
                default:
                    throw new UnsupportedOperationException();
            }
            return DEFAULT_INSTANCE;
        }

        static {
            DEFAULT_INSTANCE.makeImmutable();
        }

        public static Blacklist getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Blacklist> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    /* loaded from: classes.dex */
    public static final class Blacklists extends GeneratedMessageLite<Blacklists, Builder> implements BlacklistsOrBuilder {
        public static final int BLACKLISTS_FIELD_NUMBER = 1;
        private static final Blacklists DEFAULT_INSTANCE = new Blacklists();
        private static volatile Parser<Blacklists> PARSER;
        private Internal.ProtobufList<Blacklist> blacklists_ = emptyProtobufList();

        private Blacklists() {
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistsOrBuilder
        public List<Blacklist> getBlacklistsList() {
            return this.blacklists_;
        }

        public List<? extends BlacklistOrBuilder> getBlacklistsOrBuilderList() {
            return this.blacklists_;
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistsOrBuilder
        public int getBlacklistsCount() {
            return this.blacklists_.size();
        }

        @Override // android.gamedriver.GameDriverProto.BlacklistsOrBuilder
        public Blacklist getBlacklists(int index) {
            return this.blacklists_.get(index);
        }

        public BlacklistOrBuilder getBlacklistsOrBuilder(int index) {
            return this.blacklists_.get(index);
        }

        private void ensureBlacklistsIsMutable() {
            if (!this.blacklists_.isModifiable()) {
                this.blacklists_ = GeneratedMessageLite.mutableCopy(this.blacklists_);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setBlacklists(int index, Blacklist value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensureBlacklistsIsMutable();
            this.blacklists_.set(index, value);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setBlacklists(int index, Blacklist.Builder builderForValue) {
            ensureBlacklistsIsMutable();
            this.blacklists_.set(index, builderForValue.build());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addBlacklists(Blacklist value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensureBlacklistsIsMutable();
            this.blacklists_.add(value);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addBlacklists(int index, Blacklist value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensureBlacklistsIsMutable();
            this.blacklists_.add(index, value);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addBlacklists(Blacklist.Builder builderForValue) {
            ensureBlacklistsIsMutable();
            this.blacklists_.add(builderForValue.build());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addBlacklists(int index, Blacklist.Builder builderForValue) {
            ensureBlacklistsIsMutable();
            this.blacklists_.add(index, builderForValue.build());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllBlacklists(Iterable<? extends Blacklist> values) {
            ensureBlacklistsIsMutable();
            AbstractMessageLite.addAll(values, this.blacklists_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearBlacklists() {
            this.blacklists_ = emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeBlacklists(int index) {
            ensureBlacklistsIsMutable();
            this.blacklists_.remove(index);
        }

        @Override // com.android.framework.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            for (int i = 0; i < this.blacklists_.size(); i++) {
                output.writeMessage(1, this.blacklists_.get(i));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.android.framework.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            for (int i = 0; i < this.blacklists_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.blacklists_.get(i));
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSerializedSize = size3;
            return size3;
        }

        public static Blacklists parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklists parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklists parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklists parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklists parseFrom(InputStream input) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklists parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklists parseDelimitedFrom(InputStream input) throws IOException {
            return (Blacklists) parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklists parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklists) parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklists parseFrom(CodedInputStream input) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklists parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Blacklists prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom((Builder) prototype);
        }

        /* loaded from: classes.dex */
        public static final class Builder extends GeneratedMessageLite.Builder<Blacklists, Builder> implements BlacklistsOrBuilder {
            private Builder() {
                super(Blacklists.DEFAULT_INSTANCE);
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistsOrBuilder
            public List<Blacklist> getBlacklistsList() {
                return Collections.unmodifiableList(((Blacklists) this.instance).getBlacklistsList());
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistsOrBuilder
            public int getBlacklistsCount() {
                return ((Blacklists) this.instance).getBlacklistsCount();
            }

            @Override // android.gamedriver.GameDriverProto.BlacklistsOrBuilder
            public Blacklist getBlacklists(int index) {
                return ((Blacklists) this.instance).getBlacklists(index);
            }

            public Builder setBlacklists(int index, Blacklist value) {
                copyOnWrite();
                ((Blacklists) this.instance).setBlacklists(index, value);
                return this;
            }

            public Builder setBlacklists(int index, Blacklist.Builder builderForValue) {
                copyOnWrite();
                ((Blacklists) this.instance).setBlacklists(index, builderForValue);
                return this;
            }

            public Builder addBlacklists(Blacklist value) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(value);
                return this;
            }

            public Builder addBlacklists(int index, Blacklist value) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(index, value);
                return this;
            }

            public Builder addBlacklists(Blacklist.Builder builderForValue) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(builderForValue);
                return this;
            }

            public Builder addBlacklists(int index, Blacklist.Builder builderForValue) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(index, builderForValue);
                return this;
            }

            public Builder addAllBlacklists(Iterable<? extends Blacklist> values) {
                copyOnWrite();
                ((Blacklists) this.instance).addAllBlacklists(values);
                return this;
            }

            public Builder clearBlacklists() {
                copyOnWrite();
                ((Blacklists) this.instance).clearBlacklists();
                return this;
            }

            public Builder removeBlacklists(int index) {
                copyOnWrite();
                ((Blacklists) this.instance).removeBlacklists(index);
                return this;
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.android.framework.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            switch (method) {
                case NEW_MUTABLE_INSTANCE:
                    return new Blacklists();
                case IS_INITIALIZED:
                    return DEFAULT_INSTANCE;
                case MAKE_IMMUTABLE:
                    this.blacklists_.makeImmutable();
                    return null;
                case NEW_BUILDER:
                    return new Builder();
                case VISIT:
                    GeneratedMessageLite.Visitor visitor = (GeneratedMessageLite.Visitor) arg0;
                    Blacklists other = (Blacklists) arg1;
                    this.blacklists_ = visitor.visitList(this.blacklists_, other.blacklists_);
                    GeneratedMessageLite.MergeFromVisitor mergeFromVisitor = GeneratedMessageLite.MergeFromVisitor.INSTANCE;
                    return this;
                case MERGE_FROM_STREAM:
                    CodedInputStream input = (CodedInputStream) arg0;
                    ExtensionRegistryLite extensionRegistry = (ExtensionRegistryLite) arg1;
                    boolean done = false;
                    while (!done) {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag != 10) {
                                if (!parseUnknownField(tag, input)) {
                                    done = true;
                                }
                            } else {
                                if (!this.blacklists_.isModifiable()) {
                                    this.blacklists_ = GeneratedMessageLite.mutableCopy(this.blacklists_);
                                }
                                this.blacklists_.add((Blacklist) input.readMessage(Blacklist.parser(), extensionRegistry));
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e.setUnfinishedMessage(this));
                        } catch (IOException e2) {
                            throw new RuntimeException(new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this));
                        }
                    }
                    break;
                case GET_DEFAULT_INSTANCE:
                    break;
                case GET_PARSER:
                    if (PARSER == null) {
                        synchronized (Blacklists.class) {
                            if (PARSER == null) {
                                PARSER = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                            }
                        }
                    }
                    return PARSER;
                default:
                    throw new UnsupportedOperationException();
            }
            return DEFAULT_INSTANCE;
        }

        static {
            DEFAULT_INSTANCE.makeImmutable();
        }

        public static Blacklists getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Blacklists> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }
}
