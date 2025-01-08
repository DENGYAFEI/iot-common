package com.iot.common.base.utils;

import com.iot.common.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark Host 相关工具类
 */
@Slf4j
public class HostUtil {

    private HostUtil() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 获取当前主机的 Local Host
     *
     * @return String
     */
    public static String localHost() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * Given an address resolve it to as many unique addresses or hostnames as can be found.
     *
     * @param address the address to resolve.
     * @return the addresses and hostnames that were resolved from {@code address}.
     */
    public static Set< String > getHostNames( String address ) {
        return getHostNames( address, true );
    }

    /**
     * Given an address resolve it to as many unique addresses or hostnames as can be found.
     *
     * @param address         the address to resolve.
     * @param includeLoopback if {@code true} loopback addresses will be included in the returned set.
     * @return the addresses and hostnames that were resolved from {@code address}.
     */
    public static Set< String > getHostNames( String address, boolean includeLoopback ) {
        Set< String > hostNames = new HashSet<>( 16 );
        try {
            InetAddress inetAddress = InetAddress.getByName( address );

            if ( inetAddress.isAnyLocalAddress() ) {
                loopbackAddresses( hostNames, includeLoopback );
            } else {
                boolean loopback = inetAddress.isLoopbackAddress();

                if ( !loopback || includeLoopback ) {
                    hostNames.add( inetAddress.getHostName() );
                    hostNames.add( inetAddress.getHostAddress() );
                    hostNames.add( inetAddress.getCanonicalHostName() );
                }
            }
        } catch ( UnknownHostException | SocketException e ) {
            log.warn( "Failed to get hostname for bind address: {}", address, e );
        }
        return hostNames;
    }

    /**
     * 获取电脑 Mac 物理地址列表
     *
     * @return Mac Array
     */
    public static List< String > localMacList() {
        ArrayList< String > macList = new ArrayList<>( 16 );
        try {
            Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces();
            while ( interfaces.hasMoreElements() ) {
                NetworkInterface networkInterface = interfaces.nextElement();
                lookupLocalMac( macList, networkInterface );
            }
            if ( !macList.isEmpty() ) {
                return macList.stream().distinct().collect( Collectors.toList() );
            }
        } catch ( Exception e ) {
            log.warn( "Failed to get local mac address" );
        }
        return macList;
    }

    /**
     * Get loopback addresses
     *
     * @param hostNames       HostName Set
     * @param includeLoopback includeLoopback if {@code true} loopback addresses will be included in the returned set.
     * @throws SocketException SocketException
     */
    private static void loopbackAddresses( Set< String > hostNames, boolean includeLoopback ) throws SocketException {
        Enumeration< NetworkInterface > interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
        for ( NetworkInterface networkInterface : Collections.list( interfaceEnumeration ) ) {
            Collections.list( networkInterface.getInetAddresses() ).forEach( inetAddress -> {
                if ( inetAddress instanceof Inet4Address ) {
                    boolean loopback = inetAddress.isLoopbackAddress();

                    if ( !loopback || includeLoopback ) {
                        hostNames.add( inetAddress.getHostName() );
                        hostNames.add( inetAddress.getHostAddress() );
                        hostNames.add( inetAddress.getCanonicalHostName() );
                    }
                }
            } );
        }
    }

    /**
     * Lookup local mac
     *
     * @param macList          Mac List
     * @param networkInterface NetworkInterface
     * @throws SocketException SocketException
     */
    private static void lookupLocalMac( ArrayList< String > macList, NetworkInterface networkInterface ) throws SocketException {
        List< InterfaceAddress > interfaceAddressList = networkInterface.getInterfaceAddresses();
        for ( InterfaceAddress interfaceAddress : interfaceAddressList ) {
            InetAddress inetAddress = interfaceAddress.getAddress();
            NetworkInterface network = NetworkInterface.getByInetAddress( inetAddress );
            if ( network == null ) {
                continue;
            }
            byte[] mac = network.getHardwareAddress();
            if ( mac != null ) {
                StringBuilder stringBuilder = new StringBuilder();
                for ( int i = 0; i < mac.length; i++ ) {
                    stringBuilder.append( String.format( "%02X%s", mac[i], ( i < mac.length - 1 ) ? "-" : "" ) );
                }
                macList.add( stringBuilder.toString() );
            }
        }
    }

}
