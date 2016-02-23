#!/usr/bin/env groovy
// script to read all available euscreen ids from noterik server into stdout


def noterick = "http://c6.noterik.com/domain/euscreen/user/"
def providers = new URL( noterick ).readLines()

for( provider in providers ) {
	if( !provider.startsWith( "eu") ) continue;
	def url = noterick+provider+"/"
	def ids = new URL(url).readLines()
	for( id in ids ) {
		println id;
	}
}

