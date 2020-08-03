package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.dao.UrlRepository;
import com.qurlapi.qurlapi.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(final UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    public void addUrl(final Url url) {
        urlRepository.save(url);
    }
}
