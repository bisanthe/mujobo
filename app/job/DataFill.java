package job;

import com.mujobo.solr.Solr;
import com.mujobo.solr.model.JobDoc;
import models.Company;
import play.Logger;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

import java.util.List;

@OnApplicationStart
public class DataFill extends play.jobs.Job {
	public void doJob() throws Exception {
		if(Company.count() == 0) {
            Fixtures.deleteDatabase();
            Logger.info("Loading datafill");
            Fixtures.loadModels("datafill.yml");
		}
        //importJobs2Solr();
	}

    public void importJobs2Solr(){
        List<models.Job> jobList = models.Job.findAll();
        for(models.Job job : jobList){
           String description = job.getDescription();
            description = description.replaceAll("<b>", "").replaceAll("</b>", "");
            job.setDescription(description);
            job.save();
            JobDoc solrJobDoc = new JobDoc(job);
            Solr.saveJob(solrJobDoc);
        }
    }
}
