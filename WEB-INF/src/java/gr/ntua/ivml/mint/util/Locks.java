package gr.ntua.ivml.mint.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * In memory locking system.
 *  There are read and write locks
 *  Lockholders can be Users, Threads, or HttpSessions
 *  (for convenience, the lockholder is a String)
 *  
 *  You can lock Lockable objects that have a lockName and lockId.
 *  
 *  A read lock on an object can be obtained if there is no write lock...
 *  A write lock can be obtained if there is no lock on an object (from a different holder).
 *  
 *  Read locks are counted, so if a holder holds 2 read locks (or more) he has to either 
 *  release them 2 or more times or call Locks.release( holder )
 *  
 *  Usage: To aquire locks
 *  Locks l = Locks.holder( "User[12]" )
 *  	.read( dataset )
 *  	.write( mapping )
 *  	.aquire();
 *  if( l == null ) locks not retrieved
 *  
 *  To release Locks
 *  l.release()
 *  
 *  Locks.release( "User[12]" );
 * @author Arne Stabenau 
 *
 */
public class Locks {
	private String lockHolder;
	private List<Lock> locks;
	
	// all Locks by object locked
	private static Map<String,List<Lock>> allLocks = new HashMap<String, List<Lock>>();
	
	public static class Lock {
		public boolean write;
		public String lockHolder;
		public String lockedObject;
		public String lockName;
		public Date aquired;
		public int count;
		
		public String toString() {
			return String.format( "%s lock on %s by %s at 4$%tD 4$%tT",
					(write?"Write":"Read"),
					lockName,
					lockHolder,
					aquired );
		}
	}
	
	/**
	 * Try to aquire the given locks.  
	 * 
	 * @param locks
	 * @return the aquired Locks or null if it fails
	 */
	public Locks aquire( ) {
		synchronized( Locks.class ) {
			if( ! canAquire()) return null;
			else {
				for( Lock l: locks ) {
					aquire( l );
				}
			}
			return this;
		}
	}
	
	public static Locks holder( String holder ) {
		Locks res = new Locks();
		res.lockHolder = holder;
		return res;
	}
 	
	/**
	 * Releases all locks from lockHolder.
	 * @param lockHolder
	 */
	public static synchronized void release( String lockHolder ) {
		for( List<Lock> ll: allLocks.values()) {
			Iterator<Lock> i = ll.iterator();
			while( i.hasNext()) {
				Lock l2 = i.next();
				if( l2.lockHolder.equals( lockHolder )) i.remove();
			}
		}
	}
	
	/**
	 * Releases given locks.
	 * @param locks
	 */
	public void release() {
		synchronized( Locks.class ) {
			for( Lock l: locks) releaseLock( l );
		}
	}
	
	private static void releaseLock( Lock l ) {
		List<Lock> ll = allLocks.get( l.lockedObject );
		Iterator<Lock> i = ll.iterator();
		while( i.hasNext() ) {
			Lock l2 = i.next();
			if( l2.lockHolder.equals( l.lockHolder )) {
				if( l.write ) {
					i.remove();
				} else {
					if( l2.count > 1 ) l2.count--;
					else i.remove();
				}
			}
		}
	}
	
	private Locks addLock( Lockable lo, boolean write ) {
		Lock l = new Lock();
		l.lockedObject = lo.lockId();
		l.lockName = lo.lockName();
		l.write = write;
		l.lockHolder = lockHolder;
		locks.add( l );
		return this;		
	}
	
	/**
	 * Add a read lock request to the current Locks request
	 * @param lo
	 * @return
	 */
	public Locks read( Lockable lo ) {
		return addLock( lo, false );
	}

	/**
	 * Add a write lock request to the current Locks request
	 * @param lo
	 * @return
	 */
	public Locks write( Lockable lo ) {
		return addLock( lo, true );
	}

	/**
	 * Check if this locks object can be aquired from allLocks
	 * @return
	 */
	private boolean canAquire( ) {
		for( Lock l: locks ) 
			if( ! canAquire( l )) return false;
		return true;
	}
	
	private static boolean canAquire( Lock l ) {
		List<Lock> ll = allLocks.get(l.lockedObject);
		if(( ll == null ) || ( ll.size() == 0 )) return true;
		if( l.write ) {
			Lock l2 = ll.get(0);
			if( l2.lockHolder.equals(l.lockHolder) &&
					l2.write ) return true;
			else return false;
		} else {
			for( Lock l2: ll ) 
				if( l2.write) return false;
			return true;
		}
	}
	
	private static void aquire( Lock l ) {
		l.aquired = new Date();
		List<Lock> ll = allLocks.get( l.lockedObject );
		if( ll==null ) {
			ll = new ArrayList<Lock>();
			allLocks.put( l.lockedObject, ll);
			l.count = 1;
			ll.add( l );
			return;
		}
		// we know we can aquire it, so the list is either empty or
		// the write lock belongs to lockholder
		if( l.write ) {
			if( ll.isEmpty() ) {
				ll.add( l );
				l.count = 1;
			}
		} else {
			boolean added = false;
			for( Lock l2: ll ) {
				if( l2.lockHolder.equals( l.lockHolder )) {
					l2.count++;
					added = true;
					break;
				}
			}
			if( ! added ) {
				l.count = 1;
				ll.add( l );
			}
		}
	}
	
	/**
	 * Should a read op be allowed to happen.
	 * @param l
	 * @return
	 */
	public static boolean isReadable( Lockable l ) {
		List<Lock> ll = allLocks.get(l.lockId());
		if(( ll == null ) || (ll.isEmpty())) return true;
		for( Lock l2: ll ) {
			if( l2.write ) return false;
		}
		return true;
	}
	
	/**
	 * Generally checks for writeability of Lockable item.
	 * Does not check whether the lock owner asks for writeability.
	 * @param l
	 * @return
	 */
	public static boolean isWritable( Lockable l ) {
		List<Lock> ll = allLocks.get(l.lockId());
		if(( ll == null ) || (ll.isEmpty())) return true;
		return false;
	}
	
	
}
