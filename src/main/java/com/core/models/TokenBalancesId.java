package com.core.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class TokenBalancesId implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    private Long tokenId;
    private Long walletId;
 
    public TokenBalancesId() {
 
    }
 
    public TokenBalancesId(Long tokenId, Long walletId) {
        super();
        this.tokenId = tokenId;
        this.walletId = walletId;
    }
 
    public Long getTokenId() {
        return tokenId;
    }
 
    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }
 
    public Long getWalletId() {
        return walletId;
    }
 
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
 
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((tokenId == null) ? 0 : tokenId.hashCode());
        result = prime * result
                + ((walletId == null) ? 0 : walletId.hashCode());
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TokenBalancesId other = (TokenBalancesId) obj;
        return Objects.equals(getTokenId(), other.getTokenId()) && Objects.equals(getWalletId(), other.getWalletId());
    }
}