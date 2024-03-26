package dev.willem.wheelcoin;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

// https://docs.papermc.io/paper/dev/getting-started/paper-plugins#loaders
public final class WheelcoinLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact("org.mongodb:mongodb-driver-sync:5.0.0"), null));
        resolver.addRepository(new RemoteRepository.Builder("maven", "default", "https://repo.maven.apache.org/maven2").build());

        classpathBuilder.addLibrary(resolver);
    }
}
