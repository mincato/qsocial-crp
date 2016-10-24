package com.qsocialnow.common.model.event;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class Event implements Serializable {

    /**
		 * 
		 */
    private static final long serialVersionUID = -1414492128433530902L;

    private Long timestamp;

    private Long tokenId;

    private Long[] seriesId;

    private Long[] subSeriesId;

    private String id;

    private String idOriginal;

    private Long medioId;

    private String tipoDeMedio;

    private Short connotacion;

    private Long seccionExternaOrigen;

    private String sistemaOrigenExterno;

    private Date fecha;

    private Boolean esTapa;

    private String usuarioOriginal;

    private String usuarioCreacion;

    private String usuarioReproduccion;

    private String usuarioCategorizador;

    private Date fechaCategorizacion;

    private Integer noticiaStatus;

    private Integer odaCategorizada;

    private Boolean esSecundario;

    private String idPadre;

    private Boolean esLike;

    private Boolean esReproduccion;

    private Date fechaCreacion;

    private Date versionDiccionarioActores;

    private Date versionTemas;

    private Long reproduccionesCount;

    private Long likeCount;

    private String titulo;

    private String texto;

    private String normalizeMessage;

    private String urlNoticia;

    private String urlMultimediaContent;

    private String volanta;

    private String bajada;

    private String copete;

    private Long followersCount;

    private String idUsuarioOriginal;
    /**
     * IdUsuarioDeRetweet
     */
    private String idUsuarioReproduccion;

    /**
     * IdUsuarioOriginal
     */
    private String idUsuarioCreacion;

    private String profileImage;

    private String name;

    private String language;

    private String originalLocation;

    private EventLocation location;

    private GeoSocialEventLocationMethod locationMethod;

    /* Para medios masivos */

    private String[] menciones;

    private String[] actores;

    private String[] hashTags;

    private Long[] areasTematicas;

    private String[] hotTopics;

    private Long[] temas;

    private Long[] categorias;

    private Long[] conjuntos;

    /*
     * es un mapa idActor:List<Long> con id de atributos) (no parece que deba
     * estar indexado)
     */

    private Map<Long, Long[]> atributosActores;

    /* Unidades administrativas */
    private Long continent;

    private Long country;

    private Long adm1;

    private Long adm2;

    private Long adm3;

    private Long adm4;

    private Long city;

    private Long neighborhood;

    private boolean responseDetected;

    private String originIdCase;
    
    private String rootCommentId;

    public Event() {

    }

    public String[] getActores() {
        return actores;
    }

    public void setActores(String[] actores) {
        this.actores = actores;
    }

    public Long[] getAreasTematicas() {
        return areasTematicas;
    }

    public void setAreasTematicas(Long[] areasTematicas) {
        this.areasTematicas = areasTematicas;
    }

    public String[] getHotTopics() {
        return hotTopics;
    }

    public void setHotTopics(String[] hotTopics) {
        this.hotTopics = hotTopics;
    }

    public Long[] getTemas() {
        return temas;
    }

    public void setTemas(Long[] temas) {
        this.temas = temas;
    }

    public Map<Long, Long[]> getAtributosActores() {
        return atributosActores;
    }

    public void setAtributosActores(Map<Long, Long[]> atributosActores) {
        this.atributosActores = atributosActores;
    }

    public String[] getHashTags() {
        return hashTags;
    }

    public void setHashTags(String[] hashTags) {
        this.hashTags = hashTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMedioId() {
        return medioId;
    }

    public void setMedioId(Long medioId) {
        this.medioId = medioId;
    }

    public Short getConnotacion() {
        return connotacion;
    }

    public void setConnotacion(Short connotacion) {
        this.connotacion = connotacion;
    }

    public Long getSeccionExternaOrigen() {
        return seccionExternaOrigen;
    }

    public void setSeccionExternaOrigen(Long seccionExternaOrigen) {
        this.seccionExternaOrigen = seccionExternaOrigen;
    }

    public String getSistemaOrigenExterno() {
        return sistemaOrigenExterno;
    }

    public void setSistemaOrigenExterno(String sistemaOrigenExterno) {
        this.sistemaOrigenExterno = sistemaOrigenExterno;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getEsTapa() {
        return esTapa;
    }

    public void setEsTapa(Boolean esTapa) {
        this.esTapa = esTapa;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioCategorizador() {
        return usuarioCategorizador;
    }

    public void setUsuarioCategorizador(String usuarioCategorizador) {
        this.usuarioCategorizador = usuarioCategorizador;
    }

    public Date getFechaCategorizacion() {
        return fechaCategorizacion;
    }

    public void setFechaCategorizacion(Date fechaCategorizacion) {
        this.fechaCategorizacion = fechaCategorizacion;
    }

    public Integer getNoticiaStatus() {
        return noticiaStatus;
    }

    public void setNoticiaStatus(Integer noticiaStatus) {
        this.noticiaStatus = noticiaStatus;
    }

    public Integer getOdaCategorizada() {
        return odaCategorizada;
    }

    public void setOdaCategorizada(Integer odaCategorizada) {
        this.odaCategorizada = odaCategorizada;
    }

    public Boolean getEsSecundario() {
        return esSecundario;
    }

    public void setEsSecundario(Boolean esSecundario) {
        this.esSecundario = esSecundario;
    }

    public String getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(String idPadre) {
        this.idPadre = idPadre;
    }

    public Boolean getEsLike() {
        return esLike;
    }

    public void setEsLike(Boolean esLike) {
        this.esLike = esLike;
    }

    public Boolean getEsReproduccion() {
        return esReproduccion;
    }

    public void setEsReproduccion(Boolean esReproduccion) {
        this.esReproduccion = esReproduccion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getVersionDiccionarioActores() {
        return versionDiccionarioActores;
    }

    public void setVersionDiccionarioActores(Date versionDiccionarioActores) {
        this.versionDiccionarioActores = versionDiccionarioActores;
    }

    public Date getVersionTemas() {
        return versionTemas;
    }

    public void setVersionTemas(Date versionTemas) {
        this.versionTemas = versionTemas;
    }

    public Long getReproduccionesCount() {
        return reproduccionesCount;
    }

    public void setReproduccionesCount(Long reproduccionesCount) {
        this.reproduccionesCount = reproduccionesCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrlNoticia() {
        return urlNoticia;
    }

    public void setUrlNoticia(String urlNoticia) {
        this.urlNoticia = urlNoticia;
    }

    public String getUrlMultimediaContent() {
        return urlMultimediaContent;
    }

    public void setUrlMultimediaContent(String urlMultimediaContent) {
        this.urlMultimediaContent = urlMultimediaContent;
    }

    public String getVolanta() {
        return volanta;
    }

    public void setVolanta(String volanta) {
        this.volanta = volanta;
    }

    public String getBajada() {
        return bajada;
    }

    public void setBajada(String bajada) {
        this.bajada = bajada;
    }

    public String getCopete() {
        return copete;
    }

    public void setCopete(String copete) {
        this.copete = copete;
    }

    public String getUsuarioReproduccion() {
        return usuarioReproduccion;
    }

    public void setUsuarioReproduccion(String usuarioReproduccion) {
        this.usuarioReproduccion = usuarioReproduccion;
    }

    public String getUsuarioOriginal() {
        return usuarioOriginal;
    }

    public void setUsuarioOriginal(String usuarioOriginal) {
        this.usuarioOriginal = usuarioOriginal;
    }

    public Long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount;
    }

    public String getIdUsuarioReproduccion() {
        return idUsuarioReproduccion;
    }

    public void setIdUsuarioReproduccion(String idUsuarioReproduccion) {
        this.idUsuarioReproduccion = idUsuarioReproduccion;
    }

    public String getIdUsuarioCreacion() {
        return idUsuarioCreacion;
    }

    public void setIdUsuarioCreacion(String idUsuarioCreacion) {
        this.idUsuarioCreacion = idUsuarioCreacion;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public EventLocation getLocation() {
        return location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTipoDeMedio() {
        return tipoDeMedio;
    }

    public void setTipoDeMedio(String tipoDeMedio) {
        this.tipoDeMedio = tipoDeMedio;
    }

    public void setIdUsuarioOriginal(String idUsuarioOriginal) {
        this.idUsuarioOriginal = idUsuarioOriginal;
    }

    public String getIdUsuarioOriginal() {
        return idUsuarioOriginal;
    }

    public void setMenciones(String[] menciones) {
        this.menciones = menciones;
    }

    public String[] getMenciones() {
        return menciones;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long[] getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Long[] seriesId) {
        this.seriesId = seriesId;
    }

    public Long[] getSubSeriesId() {
        return subSeriesId;
    }

    public void setSubSeriesId(Long[] subSeriesId) {
        this.subSeriesId = subSeriesId;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation) {
        this.originalLocation = originalLocation;
    }

    public GeoSocialEventLocationMethod getLocationMethod() {
        return locationMethod;
    }

    public void setLocationMethod(GeoSocialEventLocationMethod locationMethod) {
        this.locationMethod = locationMethod;
    }

    public void setNormalizeMessage(String normalizeMessage) {
        this.normalizeMessage = normalizeMessage;
    }

    public String getNormalizeMessage() {
        return normalizeMessage;
    }

    public void setIdOriginal(String idOriginal) {
        this.idOriginal = idOriginal;
    }

    public String getIdOriginal() {
        return idOriginal;
    }

    public void setConjuntos(Long[] conjuntos) {
        this.conjuntos = conjuntos;
    }

    public Long[] getConjuntos() {
        return conjuntos;
    }

    public void setCategorias(Long[] categorias) {
        this.categorias = categorias;
    }

    public Long[] getCategorias() {
        return categorias;
    }

    public Long getContinent() {
        return continent;
    }

    public void setContinent(Long continent) {
        this.continent = continent;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getAdm1() {
        return adm1;
    }

    public void setAdm1(Long adm1) {
        this.adm1 = adm1;
    }

    public Long getAdm2() {
        return adm2;
    }

    public void setAdm2(Long adm2) {
        this.adm2 = adm2;
    }

    public Long getAdm3() {
        return adm3;
    }

    public void setAdm3(Long adm3) {
        this.adm3 = adm3;
    }

    public Long getAdm4() {
        return adm4;
    }

    public void setAdm4(Long adm4) {
        this.adm4 = adm4;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Long neighborhood) {
        this.neighborhood = neighborhood;
    }

    public boolean isResponseDetected() {
        return responseDetected;
    }

    public void setResponseDetected(boolean responseDetected) {
        this.responseDetected = responseDetected;
    }

    public String getOriginIdCase() {
        return originIdCase;
    }

    public void setOriginIdCase(String originIdCase) {
        this.originIdCase = originIdCase;
    }
    
    public String getRootCommentId() {
		return rootCommentId;
	}
    
    public void setRootCommentId(String rootCommentId) {
		this.rootCommentId = rootCommentId;
	}

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
