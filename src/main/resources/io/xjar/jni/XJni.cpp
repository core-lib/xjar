#include "XJni.h"

JNIEXPORT jobject JNICALL Java_io_xjar_XJni_call(JNIEnv* env, jobject thiz)
{
	const jbyte bytes[] = {<%
	for (k in xKey.decryptKey) {
	    print(k);
	    if (!kLP.last) {
	        print(', ');
	    }
	}
	%>};
	const char* algorithm = "${xKey.algorithm}";
    jint keysize = ${xKey.keysize};
    jint ivsize = ${xKey.ivsize};
    const char* password = "";
    const jbyte secretKey[] = { <%
    for (k in xKey.secretKey) {
        print(k);
        if (!kLP.last) {
            print(', ');
        }
    }
    %> };
    jbyteArray key = env->NewByteArray(sizeof(secretKey) / sizeof(secretKey[0]));
    env->SetByteArrayRegion(key, 0, sizeof(secretKey) / sizeof(secretKey[0]), secretKey);

    const jbyte ivParameter[] = { <%
    for (k in xKey.ivParameter) {
      print(k);
      if (!kLP.last) {
          print(', ');
      }
    }
    %> };
    jbyteArray iv = env->NewByteArray(sizeof(ivParameter) / sizeof(ivParameter[0]));
    env->SetByteArrayRegion(iv, 0, sizeof(ivParameter) / sizeof(ivParameter[0]), ivParameter);

    jclass keyClass = env->FindClass("io/xjar/key/XSymmetricSecureKey");
    jmethodID constructor = env->GetMethodID(keyClass, "<init>", "(Ljava/lang/String;IILjava/lang/String;[B[B)V");
    return env->NewObject(
        keyClass,
        constructor,
        env->NewStringUTF(algorithm),
        keysize,
        ivsize,
        env->NewStringUTF(password),
        key,
        iv
    );
}