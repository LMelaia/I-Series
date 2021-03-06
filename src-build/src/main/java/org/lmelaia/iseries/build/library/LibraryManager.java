/*   Copyright (C) 2016  Luke Melaia
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.build.library;

import org.lmelaia.iseries.build.utils.CopyFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a set (or list) of libraries that need to be copied over in order
 * for the application to work.
 *
 * <p>
 * It is done this way to avoid packaging a library without also packaging it's
 * licence.
 *
 * @author Luke Melaia
 */
public class LibraryManager {

    /**
     * The folder containing the library jar files.
     */
    private final File from;

    /**
     * The folder to which the library files will be copied to.
     */
    private final File to;

    /**
     * The folder to where the licences will be copied.
     */
    private final File legal;

    /**
     * The list of libraries the root project depends on.
     */
    private final List<Library> libraries;

    /**
     * Constructs a new library manager.
     *
     * @param sourceFolder the folder containing the library jar files.
     * @param destFolder the folder to where the libraries will be copied.
     * @param legalFolder the folder to where the licences will be copied.
     */
    public LibraryManager(File sourceFolder, File destFolder, File legalFolder){
        if (!Objects.requireNonNull(sourceFolder).exists()) {
            throw new IllegalArgumentException("The source folder must exist");
        }

        if (!sourceFolder.isDirectory()) {
            throw new IllegalArgumentException(
                    "The source folder must be a directory");
        }

        this.from = sourceFolder;
        this.to = Objects.requireNonNull(destFolder);
        this.legal = Objects.requireNonNull(legalFolder);
        libraries = new ArrayList<>();
    }

    /**
     * Adds a library to the list.
     *
     * @param library the library to add.
     * @return {@code true} if the library was added, {@code false} otherwise.
     * @throws FileNotFoundException if the library jar file cannot be found.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addLibrary(Library library) throws FileNotFoundException {
        setLibraryCopyFiles(library);
        return this.libraries.add(Objects.requireNonNull(library));
    }

    /**
     * Removes a library from the list.
     *
     * @param library the library to remove.
     * @return {@code true} if the library was removed, {@code false} otherwise.
     */
    @SuppressWarnings("unused")
    public boolean removeLibrary(Library library) {
        return this.libraries.remove(Objects.requireNonNull(library));
    }

    /**
     * Copies the library files and licence files over to the correct folders.
     *
     * @return a library package containing the library and licence files.
     * @throws IOException if a file cannot be copied over.
     */
    @SuppressWarnings("UnusedReturnValue")
    public LibraryPackage copyOver() throws IOException {
        List<File> libraryFiles = new ArrayList<>();
        List<File> licenceFiles = new ArrayList<>();

        for (Library library : libraries) {
            if(library.getFileName() != null){
                libraryFiles.add(library.getLibraryFile().copy());
                licenceFiles.add(library.getLicenceFile().copy());
            }else{
                for (CopyFile file : library.getLibraryFiles()) {
                    libraryFiles.add(file.copy());
                }

                licenceFiles.add(library.getLicenceFile().copy());
            }

        }
        
        return new LibraryPackage(
                libraryFiles.toArray(new File[libraryFiles.size()]),
                licenceFiles.toArray(new File[licenceFiles.size()]));
    }

    /**
     * Constructs and sets the {@link CopyFile}s for the library.
     *
     * @param library the library to initialize the copy files for.
     * @throws FileNotFoundException if the libraries jar file cannot be found.
     */
    private void setLibraryCopyFiles(Library library)
            throws FileNotFoundException {
        library.setLicenceFile(new CopyFile(library.getLicence().getFile(),
                legal, library.getName() + " Licence.txt"));

        File libraryFile;

        if(library.getFileName() != null) {
            libraryFile = new File(
                    from.getAbsoluteFile() + "/" + library.getFileName());

            if (!libraryFile.exists()) {
                throw new java.io.FileNotFoundException(
                        "The library file: " + libraryFile + " does not exist");
            }

            library.setLibraryFile(new CopyFile(libraryFile, to));
        }else{
            List<CopyFile> names = new ArrayList<>();

            for (String fileName : library.getFileNames()) {
                libraryFile = new File(
                        from.getAbsoluteFile() + "/" + fileName + ".jar");

                if (!libraryFile.exists()) {
                    throw new java.io.FileNotFoundException(
                            "The library file: " + libraryFile + " does not exist");
                }

                names.add(new CopyFile(libraryFile, to));
            }

            library.setLibraryFiles(names.toArray(new CopyFile[names.size()]));
        }
    }
}
