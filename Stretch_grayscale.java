// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 						// pour classe Rectangle

// Nom de la classe = nom du fichier.  Implémente l'interface PlugInFilter
public class Stretch_grayscale implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}

	public void run(ImageProcessor ip) {
		Rectangle r = ip.getRoi(); // Région d'intérêt sélectionnée (r.x=r.y=0 si aucune)

		int distrib = 256;

		//on crée un tableau de taille 256 contenant qui à un niveau de gris associe le nombre de pixels correspondant
		int histo[] = new int[distrib];

		//on parcours tous les pixels de r en incrémentant la valeur indexée par son niveau de gris
		//on obtient ainsi l'histogramme de r
		for (int y=r.y; y<(r.y+r.height); y++)
			for (int x=r.x; x<(r.x+r.width); x++)
				histo[(int)ip.get(x,y)]++;

		//recherche du min et du max
		int histomin=0, histomax=distrib-1;
		boolean minOK=false, maxOK=false;
		for(int c=0; c<distrib;c++){
			if(histo[c]==0){
				if(!minOK)
					histomin=c;
			}else{
				minOK=true;
			}

			if(histo[distrib-1-c]==0){
				if(!maxOK)
					histomax=distrib-1-c;
			}else{
				maxOK=true;
			}
		}

		//initialisation de la lut
		double LUT[] = new double[distrib];

		for(int ng=0;ng<distrib;ng++){
			LUT[ng] = ((double)distrib-1)*((double)(ng-histomin))/((double)(histomax-histomin));
		}

		//calcul de la transformation
		for (int yt=r.y; yt<(r.y+r.height); yt++)
			for (int xt=r.x; xt<(r.x+r.width); xt++)
				ip.set(xt, yt, (int)LUT[(int)ip.get(xt, yt)]);

	}

}
