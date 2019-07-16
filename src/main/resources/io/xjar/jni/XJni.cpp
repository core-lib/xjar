#include "XJni.h"

JNIEXPORT jbyteArray JNICALL Java_io_xjar_XJni_call(JNIEnv* env, jobject thiz)
{
	const jbyte bytes[] = {<%
	for (k in xKey.decryptKey) {
	    print(k);
	    if (!kLP.last) {
	        print(', ');
	    }
	}
	%>};
	int len = sizeof(bytes) / sizeof(bytes[0]);
	jbyteArray key = env->NewByteArray(len);
	env->SetByteArrayRegion(key, 0, len, bytes);
	return key;
}