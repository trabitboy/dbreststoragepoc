package org.trab.test.dbreststorage.service.impl;

public class TestPkg {

	public TestPkg(long pkgId, long latestMivId, long oldestMivId) {
		super();
		this.pkgId = pkgId;
		//this.mavId = mavId;
		this.latestMivId = latestMivId;
		this.oldestMivId = oldestMivId;
	}
	public long pkgId;
	//public long mavId;
	public long latestMivId;
	public long oldestMivId;
	
}
