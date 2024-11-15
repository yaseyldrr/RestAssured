package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Location {
    /*
    {
    "post code": "90210",
    "country": "United States",
    "country abbreviation": "US",
    "places": [
        {
            "place name": "Beverly Hills",
            "longitude": "-118.4065",
            "state": "California",
            "state abbreviation": "CA",
            "latitude": "34.0901"
        }
    ]
}
     */
    private String postcode;
    private String country;
    private String countryabbreviation;
    private List<Place> places;

    public String getPostcode() {
        return postcode;
    }

    @JsonProperty("post code")
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryabbreviation() {
        return countryabbreviation;
    }

    @JsonProperty("country abbreviation")
    public void setCountryabbreviation(String countryabbreviation) {
        this.countryabbreviation = countryabbreviation;
    }

    public List<Place> getPlaces() {
        return places;
    }

    @Override
    public String toString() {
        return "Location{" +
                "postCode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", countryAbbreviation='" + countryabbreviation + '\'' +
                ", places=" + places +
                '}';
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
