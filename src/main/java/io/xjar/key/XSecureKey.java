package io.xjar.key;

import java.io.Serializable;

/**
 * 密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public abstract class XSecureKey implements XKey, Serializable {
	private static final long serialVersionUID = -5577962754674149355L;

	protected final String algorithm;
	protected final int size;

	public XSecureKey(String algorithm, int size) {
		super();
		this.algorithm = algorithm;
		this.size = size;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public int getSize() {
		return size;
	}

}
