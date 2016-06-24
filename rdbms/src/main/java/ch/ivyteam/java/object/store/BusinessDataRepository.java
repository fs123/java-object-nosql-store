package ch.ivyteam.java.object.store;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessDataRepositoryExtended.Updater;

/**
 * Draft of an object repository for Business Data
 * 
 * @since 22.06.2016
 */
public interface BusinessDataRepository
{
	/**
	 * TODO: Does it save to the database? (Use case for not storing on create: same behavior for update and create (e.g. JSF Dialog)
	 */
  <T> BusinessData<T> create(T obj);
  

  /**
   * Retrieves BusinessData
   * TODO: why find? find, query / Spring Data: getOne
   * @param id TODO: switch to ValueObject
   * @return Null if not exists
   */
  <T> BusinessData<T> find(Long id);
  
  /**
   * Checks for existence of a BusinessData (is likely faster than find)
   * @param id
   * @return
   */
  boolean exists(Long id);

  /**
   * Executes the updater on the Business object with the given id.
   * Because of Concurrency Issues, the Updater could be executed multiple times, until the update could be successfully executed.
   */
  <T> boolean update(Long id, Updater<T> updater);
  
  /**
   * Query directly on the system db.
   * Idea: a Query API and a Search API
   * Query API: query directly on system db, no discrepancy between query and result
   * Search API: search in external search system (elastic search). => faster, but search result might not be consistence with the contents of the system db
   * 
   * Drawbacks of the Query API:
   * - search for contents of a field by name (without hierarchy)
   * - to search hierarchically the object would have to be saved a second time (e.g. owner.person.name = "Aaron") with resolved references
   * - only text matches = no range queries possible (e.g. date from 2016-06-24 to 2017-01-01)
   * 
   * Preselect with the Query API. Narrow down with the streaming API in Java code.
   * <h1 style="color:red">Preliminary!</h1>
   * @param resultType
   * @return fluid query
   */
  public <T> FluentQuery<T> query(Class<T> resultType);


  /**
   * <p>Textual search on field contents</p>
   * Sample: <code><pre>Dossier aronsDossier = repository().query(Dossier.class)
              .field("firstName").isEqualTo("Aron")
              .execute().objects().get(0)</pre></code>
   * <h1 style="color:red">Preliminary!</h1>
   * @param resultType
   * @return fluid query
   */
  public static interface FluentQuery<T>
  {
    public FieldOperator<T> field(String fieldName);
    
    public static interface FieldOperator<T>
    {
      FluentQuery<T> contains(String value);
      FluentQuery<T> isEqualTo(String value);
      FluentQuery<T> isEqualTo(Number value);
    }
    
    QueryResult<T> execute();
    
    /**
     * Contains the data from the db.
     * Basically, it is a holder of a List of BusinessData<T>. 
     */
    public static interface QueryResult<T> 
    {
    	/**
    	 * Returns a list to the (references) of the deserialized objects.
    	 * Do we need this (yet)?
    	 */
      List<T> objects();
      
      /**
       * List of serialized Business data objects 
       */
      List<BusinessData<T>> data();
      
      /**
       * Prio 2
       * Streaming api to narrow down on the result
       * TODO: rename
       */
      public static interface DocumentResImpl<T>
      {
        Stream<BusinessData<T>> docStream();
        Stream<T> dataStream();
      }
    }
  }
  
  /**
   * Prio 2
   */
  public static interface BusinessDataRepositoryExtended<T>
  {
    Set<Long> create(List<T> obj); 
    Set<T> findAll();
    boolean delete(Long id);
    
    UpdateResults update(FluentQuery<T> query, Updater<T> updater);
    
    public static interface UpdateResults
    {
    	long getUpdatedCount();
    }

    @FunctionalInterface
    public static interface Updater<T>
    {
      void update(T object); 
    }
  }
  
  /**
   * Represents one Entry from the database
   * Contains all raw data of the db, The object is lazy deserialized (e.g. by calling object())
   */
  public static interface BusinessData<T> {
    MetaData<T> getMeta();
    
    T object();
    
    public static interface DocumentImpl
    {
      String rawData();
    }
    
    /**
     * Fields stored in the db, besides the actual object
     */
    public static interface MetaData<T> {
      Long getId();
      
      /**
       * Incremented on each db update.
       * used to handle concurrent modification and prevent data loss
       */
      long getVersion();
      String getTypeRaw();
      Class<T> getType();
    }

    /**
     * saves the object if the current version in the db
     * is the same as the version store in the current BusinessData meta.
     * returns true if successful
     */
    boolean save();
    
    /**
     * stores the object regardless of the version
     * false, if object was already deleted
     */
    boolean overwrite();
    
    /**
     * false, if object was already deleted
     */
    boolean delete();
    
    /**
     * Checks if the current version is the same as in the DB AND the entry still exists
     */
    boolean isUpToDate();
    
    /**
     * false, if object was deleted
     */
    boolean reload();

    /**
     * Maybe useless, because we have it (now) on the Repo.
     */
    void update(Updater<T> updater);
    
    /**
     * Do we need this? (maybe move to repo)
     */
    void lockAndUpdate(Updater<T> updater);
    
  }
}
